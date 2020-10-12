package de.unibremen.swp.controller;

import de.unibremen.swp.model.User;
import de.unibremen.swp.persistence.UserDAO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

@Named
@RequestScoped
public class TemplateBean {

    /**
     * Provides the username (email) of the logged in user.
     */
    @Inject
    private Principal principal;

    /**
     * Logged in user.
     */
    private User user;

    /**
     * Used to map {@link #principal} to {@link #user}.
     */
    @Inject
    private UserDAO userDAO;

    /**
     * Session of logged in user.
     */
    @Inject
    private HttpSession session;

    /**
     * Used for redirection.
     */
    @Inject
    private ExternalContext externalContext;

    /**
     * Initializes this bean.
     */
    @PostConstruct
    private void init() {
        user = userDAO.findByEmail(principal.getName())
                .orElseThrow(SecurityException::new);
    }

    /**
     * Returns the full name of the logged in user.
     *
     * @return
     *      Pattern: 'firstName lastName'
     */
    public String getFullName() {
        return String.format("%s %s",
                user.getFirstName(),
                user.getLastName());
    }

    /**
     * Logs out the user and registers a redirection to 'login.xhtml'.
     *
     * @throws IOException
     *      If redirection failed.
     */
    public void logout() throws IOException {
        session.invalidate();
        externalContext.redirect("login.xhtml");
    }
}
