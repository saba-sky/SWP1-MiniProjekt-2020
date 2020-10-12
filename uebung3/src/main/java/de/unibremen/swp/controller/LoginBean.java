package de.unibremen.swp.controller;

import javax.faces.annotation.FacesConfig;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;


@Named
@ViewScoped
@FacesConfig
public class LoginBean implements Serializable {


    HttpSession session;

    private String email;
    private String password;

    /**
     * Our user data storage.
     */
    @Inject
    private SecurityContext securityContext;

    /**
     * Used for redirection.
     */
    @Inject
    private ExternalContext externalContext;

    /**
     * Used to add faces messages.
     */
    @Inject
    private FacesContext facesContext;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void login() throws IOException {
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        session = request.getSession(true);

        System.out.println("Email is: " + email);
        System.out.println("Password is: " + password);
        Credential credential = new UsernamePasswordCredential(getEmail(), getPassword());
        AuthenticationStatus status = securityContext.authenticate(
                request,
                response,
                AuthenticationParameters.withParams().credential(
                        credential
                )
        );

        System.out.println("Session is: " + session.getId());

        if (session != null) {
            String email = getEmail();
            String emailInSession = (String) session.getAttribute("email");
            if (emailInSession == null) {
                session.setAttribute("email", getEmail());
            }

            if (emailInSession != null && !(session.getAttribute("email").equals(email))) {
                session.removeAttribute("email");
                session.invalidate();
            }
        }


        System.out.println(session);

        if (status.equals(AuthenticationStatus.SEND_CONTINUE)) {
            // Login to continue.
            facesContext.responseComplete();
        } else if (status.equals(AuthenticationStatus.SUCCESS)) {
            // Explicit call of login page (success).
            externalContext.redirect("users.xhtml");
        } else {
            // Explicit call of login page (failure).
            final FacesMessage msg = new FacesMessage(
                    "Ungültiger Benutzeraccount");
            facesContext.addMessage(null, msg);
        }
    }

    public void invalidateSession() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        externalContext.redirect("login.xhtml");
    }
}