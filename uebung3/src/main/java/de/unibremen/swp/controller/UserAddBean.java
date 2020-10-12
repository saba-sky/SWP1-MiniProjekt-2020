package de.unibremen.swp.controller;

import de.unibremen.swp.model.User;
import de.unibremen.swp.persistence.DuplicateEmailException;
import de.unibremen.swp.persistence.UserAlreadyInsertedException;
import de.unibremen.swp.persistence.UserDAO;

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
import java.util.Objects;

/**
 * Allows to add a new user.
 */
@Named
@ViewScoped
@FacesConfig
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
    private UIComponent emailInput;

    /**
     * User to add.
     */
    private User user;

    /**
     * Initializes this bean.
     */
    @PostConstruct
    private void init() {
        user = new User();
    }

    /**
     * Returns the user to add.
     *
     * @return
     *      User to add.
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the email input text component.
     *
     * @return
     *      Email input text component.
     */
    public UIComponent getEmailInput() {
        return emailInput;
    }

    /**
     * Sets the email input text component.
     *
     * @param emailInput
     *      Email input text component to set.
     * @throws NullPointerException
     *      If {@code emailInput} is {@code null}.
     */
    public void setEmailInput(final UIComponent emailInput)
            throws NullPointerException {
        this.emailInput = Objects.requireNonNull(emailInput);
    }

    /**
     * Adds {@link #user} to {@link UserDAO}. On success, a redirect to
     * 'users.xhtml' is registered.
     *
     * @throws IOException
     *      If redirection failed.
     */
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