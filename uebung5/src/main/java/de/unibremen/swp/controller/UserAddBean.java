package de.unibremen.swp.controller;

import de.unibremen.swp.model.Role;
import de.unibremen.swp.model.User;
import de.unibremen.swp.persistence.DuplicateEmailException;
import de.unibremen.swp.persistence.UserAlreadyInsertedException;
import de.unibremen.swp.persistence.UserDAO;
import de.unibremen.swp.security.Secure;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.annotation.PostConstruct;
import javax.faces.annotation.FacesConfig;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

/**
 * Allows to add a new user.
 */
@Named
@ViewScoped
@FacesConfig
@Secure(roles = {Role.LECTURER})
public class UserAddBean implements Serializable {

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
     * The JSF input text component for {@code user.getEmail()}
     */
    @Getter
    @Setter
    @NonNull
    private UIComponent emailInput;

    /**
     * User to add.
     */
    @Getter
    private User user;

    /**
     * Initializes this bean.
     */
    @PostConstruct
    private void init() {
        user = new User();
    }

    /**
     * Adds {@link #user} to {@link UserDAO}. On success, a redirect to
     * 'users.xhtml' is registered.
     *
     * @throws IOException
     *      If redirection failed.
     */
    @Secure(roles = {Role.LECTURER})
    public void add() throws IOException {
        try {
            userDAO.insert(user);
            externalContext.redirect("users.xhtml");
        } catch (final DuplicateEmailException e) {
            final FacesMessage msg = new FacesMessage(
                    "Die E-Mail Adresse wird bereits verwendet.");
            facesContext.addMessage(emailInput.getClientId(), msg);
        } catch (final UserAlreadyInsertedException e) {
            throw new IllegalStateException(e);
        }
    }
}
