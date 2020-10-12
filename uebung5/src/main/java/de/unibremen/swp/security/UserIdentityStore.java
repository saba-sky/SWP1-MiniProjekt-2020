package de.unibremen.swp.security;

import de.unibremen.swp.model.User;
import de.unibremen.swp.persistence.UserDAO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Optional;
import java.util.Set;

/**
 * This identity store uses the data storage {@link UserDAO} to validate user
 * credentials based on email address and password (see
 * {@link UserDAO#findByEmailAndPassword(String, String)}).
 */
@RequestScoped
public class UserIdentityStore implements IdentityStore {

    /**
     * User data storage.
     */
    @Inject
    private UserDAO userDAO;

    @Override
    public CredentialValidationResult validate(final Credential credential) {
        if (!(credential instanceof UsernamePasswordCredential)) {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }

        final UsernamePasswordCredential userAndPass =
                (UsernamePasswordCredential) credential;
        final Optional<User> user = userDAO.findByEmailAndPassword(
                userAndPass.getCaller(),
                userAndPass.getPasswordAsString());
        if (user.isEmpty()) {
            return CredentialValidationResult.INVALID_RESULT;
        }

        final String callerName = user.get().getEmail();
        final Set<String> groups = Set.of(user.get().getRole().name());
        return new CredentialValidationResult(callerName, groups);
    }
}
