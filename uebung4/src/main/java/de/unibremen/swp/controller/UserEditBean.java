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

import javax.annotation.PostConstruct;
import javax.faces.annotation.FacesConfig;
import javax.faces.annotation.RequestParameterMap;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * Allows to edit a user (stored in {@link #user}). Assumes that the id of the
 * user to edit is passed as request parameter with key 'id'. If the request
 * parameter is missing or if there is no user with given id, {@link #user} is
 * {@code null}.
 */
@Getter
@Named
@ViewScoped
@FacesConfig
@Secure(roles = {Role.LECTURER, Role.TUTOR})
public class UserEditBean implements Serializable {

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
     * Used for redirection.
     */
    @Inject
    private ExternalContext externalContext;

    /**
     * Used to fetch the request parameter 'id'.
     */
    @Inject
    @RequestParameterMap
    private Map<String, String> parameterMap;

    /**
     * The JSF input text component for {@code user.getEmail()}
     */
    @Setter
    @NonNull
    private UIComponent emailInput;

    /**
     * User to edit.
     */
    private User user;

    /**
     * Initializes this bean.
     */
    @PostConstruct
    private void init() {
        final String id = parameterMap.get("id");
        user = userDAO.findById(id).orElse(null);
    }

    /**
     * Updates {@link #user}. On success, a redirect to 'users.xhtml' is
     * registered.
     *
     * @throws IOException
     *      If redirection failed.
     */
    @Secure(roles = {Role.LECTURER, Role.TUTOR})
    public void update() throws IOException {
        try {
            userDAO.update(user);
            externalContext.redirect("users.xhtml");
        } catch (final DuplicateEmailException e) {
            final FacesMessage msg = new FacesMessage(
                    "Die E-Mail Adresse wird bereits verwendet.");
            facesContext.addMessage(emailInput.getClientId(), msg);
        } catch (final NoSuchUserException e) {
            final FacesMessage msg = new FacesMessage(
                    "Die Teilnehmer*in ist nicht mehr im System.");
            facesContext.addMessage(null, msg);
        }
    }
}
