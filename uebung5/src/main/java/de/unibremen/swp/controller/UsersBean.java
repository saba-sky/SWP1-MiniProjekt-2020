package de.unibremen.swp.controller;

import de.unibremen.swp.model.Role;
import de.unibremen.swp.model.User;
import de.unibremen.swp.persistence.DuplicateEmailException;
import de.unibremen.swp.persistence.NoSuchUserException;
import de.unibremen.swp.persistence.UserDAO;
import de.unibremen.swp.security.Secure;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.CellEditEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Allows to load, delete, and update users.
 */
@Slf4j
@Named
@ViewScoped
@Secure(roles = {Role.LECTURER, Role.TUTOR, Role.STUDENT})
public class UsersBean implements Serializable {

    /**
     * Our user data storage.
     */
    @Inject
    private UserDAO userDAO;

    /**
     * Used to add faces messages.
     */
    @Inject
    private FacesContext facesContext;

    /**
     * Shows status messages.
     */
    @Getter
    @Setter
    @NonNull
    private UIComponent growl;

    /**
     * The users loaded from {@link #userDAO}.
     */
    @Getter
    private List<User> users;

    /**
     * Stores the filtered users.
     */
    @Getter
    @Setter
    private List<User> filteredUsers = null;
    private Map<String, User> editedUsers = new HashMap<>();


    /**
     * Copy of the user before it was edited
     * (cf. {@link #onCellEditInit(CellEditEvent)}).
     */
    @Getter
    @Setter
    @NonNull
    private User edit;

    /**
     * Initializes this bean.
     */
    @PostConstruct
    private void init() {
        users = userDAO.allUsers();
    }

    /**
     * Delete the given user.
     *
     * @param user User to delete.
     */
    @Secure(roles = {Role.LECTURER})
    public void delete(final User user) {
        userDAO.delete(user);
        if (filteredUsers != null) {
            filteredUsers.remove(user);
        }
        init();
    }

    /**
     * Returns either {@link #users} or {@link #filteredUsers}, depending on
     * whether a filter is active or not.
     *
     * @return The currently active model (list of user objects).
     */
    private List<User> activeModel() {
        return filteredUsers != null
                ? filteredUsers
                : users;
    }

    /**
     * Creates a copy of the user to edit. This copy is used by
     * {@link #onCellEdit(CellEditEvent)} in case of a validation error.
     *
     * @param event The event object of the ajax event.
     */
    @Secure(roles = {Role.LECTURER, Role.TUTOR})
    public void onCellEditInit(final CellEditEvent event) {
        final List<User> currentModel = findCurrentModel();
        final User userToEdit = currentModel.get(event.getRowIndex());


        final String clientId = event.getColumn().getClientId();

        log.info("Simple log statement with inputs {}, {} and {}", 1, 2, 3);
           editedUsers.put(clientId, new User(userToEdit));
    }


    private List<User> findCurrentModel() {
        if (filteredUsers != null) {
            return filteredUsers;
        } else {
            return users;
        }
    }

    /**
     * Updates the edited user.
     *
     * @param event The vent object of the ajax event.
     */
    @Secure(roles = {Role.LECTURER, Role.TUTOR})
    public void onCellEdit(final CellEditEvent event) {
        final List<User> currentModel = findCurrentModel();
        final User changedUSer = currentModel.get(event.getRowIndex());



        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (Objects.equals(oldValue,newValue)){
            return;
        }

        try {
            userDAO.update(changedUSer);
        } catch (final NoSuchUserException e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Validierung fehlgeschlageb", "Das Objekt Konnte nicht mehr gefunden werden.");
            facesContext.addMessage(growl.getClientId(), msg);
            facesContext.validationFailed();
        } catch (final DuplicateEmailException e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validierung fehlgeschlageb", "Die E-Mail Adresse wird bereits verwendet.");
            facesContext.addMessage(growl.getClientId(), msg);
            facesContext.validationFailed();
        }
        if (facesContext.isValidationFailed()) {
            final String clientId = event.getColumn().getClientId();
            final User oldUser = editedUsers.get(clientId);


            if (oldUser == null) {
                log.warn("Unable to find user for event{}",clientId);
                return ;
            }

            final User userToEdit = currentModel.get(event.getRowIndex());

             log.info("replacing by saved instance {}", userToEdit);

            users.set(users.indexOf(userToEdit), oldUser);
            log.info("old User {}", oldUser);

            if (filteredUsers != null && filteredUsers.contains(userToEdit)) {
                filteredUsers.set(filteredUsers.indexOf(userToEdit), oldUser);
            }
        }else {
            final String clientId = event.getColumn().getClientId();
            editedUsers.remove(clientId);
        }
    }

    public void onCellEditCancel(final CellEditEvent event) {
        final String clientId = event.getColumn().getClientId();
        log.info("onCellEditCancel{}",clientId);

        final User oldUser = editedUsers.remove(clientId);

        if (oldUser == null) {
            log.warn("Canceled event");
            return;
        }
        final List<User> currentModel = findCurrentModel();
        final User userToEdit = currentModel.get(event.getRowIndex());

        log.info("replacing {} by saved instance : {}",clientId);

        users.set(users.indexOf(editedUsers), oldUser);


        if (filteredUsers != null && filteredUsers.contains(userToEdit)) {
            filteredUsers.set(filteredUsers.indexOf(userToEdit), oldUser);
        }
    }
}
