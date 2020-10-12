package de.unibremen.swp.controller;

import de.unibremen.swp.model.User;
import de.unibremen.swp.persistence.DuplicateEmailException;
import de.unibremen.swp.persistence.UserDAO;
import de.unibremen.swp.service.log.Log;

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
import java.util.Objects;

/**
 * Allows to edit a user (stored in {@link #user}). Assumes that the id of the
 * user to edit is passed as request parameter with key ’id’. If the request
 * parameter is missing or if there is no user with given id, {@link #user} is
 * {@code null}.
 */
@Named
@ViewScoped
@FacesConfig
@Log
public class UsersEditBean implements Serializable {
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
     * Used to fetch the request parameter ’id’.
     */
    @Inject
    @RequestParameterMap
    private Map<String, String> parameterMap;
    /**
     * The JSF input text component for {@code user.getEmail()}.
     */
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
     * Returns the user to edit.
     *
     * @return
     * User to edit.
     */
    public User getUser() {
        return user;
    }
    /**
     * Returns the email input text component.
     *
     * @return
     * Email input text component.
     */
    public UIComponent getEmailInput() {
        return emailInput;
    }
    /**
     * Sets the email input text component.
     *
     17
     * @param emailInput
     * Email input text component to set.
     * @throws NullPointerException
     * If {@code emailInput} is {@code null}.
     */
    public void setEmailInput(final UIComponent emailInput)
            throws NullPointerException {
        this.emailInput = Objects.requireNonNull(emailInput);
    }
    /**
     * Updates {@link #user}. On success, a redirect to ’users.xhtml’ is
     * registered.
     */
    public void update() throws IOException {
        try {
            userDAO.save(user);
            externalContext.redirect("users.xhtml");
        } catch (DuplicateEmailException e) {
            final FacesMessage msg = new FacesMessage(
                    "Die E-Mail Adresse wird bereits verwendet.");
            facesContext.addMessage(emailInput.getClientId(), msg);
        }
    }
}