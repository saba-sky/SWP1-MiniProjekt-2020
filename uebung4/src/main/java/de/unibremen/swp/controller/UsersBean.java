package de.unibremen.swp.controller;

import de.unibremen.swp.model.Role;
import de.unibremen.swp.model.User;
import de.unibremen.swp.persistence.DuplicateEmailException;
import de.unibremen.swp.persistence.NoSuchUserException;
import de.unibremen.swp.persistence.UserDAO;
import de.unibremen.swp.security.Secure;
import lombok.*;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;


/**
 * Allows to load and delete users.
 */
@Data
@NoArgsConstructor
@Named
@ViewScoped
@Secure(roles = {Role.LECTURER, Role.TUTOR, Role.STUDENT})
public class UsersBean implements Serializable {

    @Setter
    User initialUser;
    /**
     * Returns all users (no copy).
     *
     * @return All users.
     */

    /**
     * Our user data storage.
     */
    @Inject
    private UserDAO userDAO;
    /**
     * The users loaded from {@link #userDAO}.
     */
    @Setter(AccessLevel.NONE)
    @NonNull
    private List<User> users;


@Setter
    private List<User> filteredUsersList;

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

        if (filteredUsersList != null) {
            filteredUsersList.remove(user);
        }
        userDAO.delete(user);
        init();
    }


    public void onCellCancel() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Diese E-Mail-Adresse wurde bereits verwendet."));
        facesContext.validationFailed();
    }

    public void onCellEdit(CellEditEvent event) throws NoSuchUserException, DuplicateEmailException {
        DataTable o = (DataTable) event.getSource();
        User selectedUser = (User) o.getRowData();

        boolean duplicate = false, dataChanged = false;

        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        String email = selectedUser.getEmail();

        Optional<User> user = userDAO.findByEmail(email);
        //get header
        String header = event.getColumn().getHeaderText();

        if (user.isPresent()) {
            switch (header) {
                case "Vorname":
                    user.get().setFirstName(String.valueOf(newValue));
                    dataChanged = true;
                    break;
                case "Nachname":
                    user.get().setLastName(String.valueOf(newValue));
                    dataChanged = true;
                    break;
                case "E-Mail":
                    for (User userWithDupEmail : users) {
                        if (newValue.equals(userWithDupEmail.getEmail())) {
                            onCellCancel();
                            duplicate = true;
                            if (filteredUsersList.size()==0){
                            users.set(event.getRowIndex(), initialUser);
                            }
                            else {
                                filteredUsersList.set(event.getRowIndex(), initialUser);
                            }
                            break;
                        }

                    }
                    if (!duplicate) {
                        user.get().setEmail(String.valueOf(newValue));
                        dataChanged = true;
                    }


                    break;
            }

        }

        if (newValue != null && !newValue.equals(oldValue) && dataChanged) {
            userDAO.update(user.get());
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }


    }
    public void onCellEditInit(CellEditEvent event) {
        Optional<User> optionalUser = userDAO.findByEmail((String) event.getOldValue());
        optionalUser.ifPresent(user -> initialUser = new User(user));
    }


}


