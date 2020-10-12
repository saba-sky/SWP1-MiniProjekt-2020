package de.unibremen.swp.security;

import de.unibremen.swp.model.User;
import de.unibremen.swp.persistence.UserDAO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@RequestScoped
public class UserIdentityStore implements IdentityStore {

    @Inject
    UserDAO userDAO;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (!(credential instanceof UsernamePasswordCredential)) {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
        Optional<User> user = userDAO.findByEmailAndPassword(
                ((UsernamePasswordCredential) credential).getCaller(),
                ((UsernamePasswordCredential) credential).getPasswordAsString());
        if (user.isPresent()) {
            return new CredentialValidationResult(((UsernamePasswordCredential) credential).getCaller()
                    , new HashSet<>(Collections.singletonList(user.get().getRole().name())));
        } else {
            return CredentialValidationResult.INVALID_RESULT;
        }
    }
}