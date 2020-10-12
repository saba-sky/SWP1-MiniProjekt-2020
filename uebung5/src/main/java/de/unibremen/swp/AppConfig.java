package de.unibremen.swp;

import javax.enterprise.context.RequestScoped;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;

/**
 * This class is used to apply the annotations that are necessary to configure
 * the application.
 */
@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/login.xhtml",
                errorPage = "/login-error.xhtml"
        )
)
@RequestScoped
public class AppConfig {
}
