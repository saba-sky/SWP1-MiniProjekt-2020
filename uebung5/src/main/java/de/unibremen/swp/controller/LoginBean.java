package de.unibremen.swp.controller;

import de.unibremen.swp.services.session.SessionService;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

/**
 * Used to log in a user. Thh user credentials are stored in {@link #email}
 * (login name) and {@link #password} (login password).
 */
@Named
@RequestScoped
public class LoginBean {

    /**
     * Login name.
     */
    @NotBlank
    @Getter
    @Setter
    private String email = "";

    /**
     * Login password.
     */
    @NotBlank
    @Getter
    @Setter
    private String password = "";

    /**
     * Used to authenticate a user with {@link #email} and {@link #password}.
     */
    @Inject
    private SecurityContext securityContext;

    /**
     * Used for redirection.
     */
    @Inject
    private ExternalContext externalContext;

    /**
     * Used to add faces messages (in case of errors) or to complete
     * authentication (in case of success).
     */
    @Inject
    private FacesContext facesContext;

    /**
     * If a user could be authenticated, this service must be notified (using
     * {@link SessionService#loggedIn(String, HttpSession)}).
     */
    @Inject
    private SessionService sessionService;

    /**
     * Authenticates a user with its login name ({@link #email}) and password
     * ({@link #password}). If the login page was called explicitly (i.e., no
     * login to continue) and the user could be authenticated, a redirection to
     * 'users.xhtml' is registered.
     *
     * @throws IOException
     *      If redirection failed.
     */
    public void login() throws IOException {
        final AuthenticationParameters credentials =
                AuthenticationParameters.withParams()
                .credential(new UsernamePasswordCredential(email, password));
        final AuthenticationStatus status = securityContext.authenticate(
                (HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse(),
                credentials);

        if (status.equals(AuthenticationStatus.SEND_CONTINUE)) {
            // Login to continue.
            facesContext.responseComplete();
            sessionService.loggedIn(email,
                    (HttpSession) externalContext.getSession(false));
        } else if (status.equals(AuthenticationStatus.SUCCESS)) {
            // Explicit call of login page (success).
            externalContext.redirect("users.xhtml");
            sessionService.loggedIn(email,
                    (HttpSession) externalContext.getSession(false));
        } else {
            // Explicit call of login page (failure).
            final FacesMessage msg = new FacesMessage(
                    "Ungültiger Benutzeraccount");
            facesContext.addMessage(null, msg);
        }
    }
}
