package de.unibremen.swp.security;

import com.lambdaworks.crypto.SCryptUtil;
import de.unibremen.swp.model.User;
import de.unibremen.swp.persistence.DuplicateEmailException;
import de.unibremen.swp.persistence.UserAlreadyInsertedException;
import de.unibremen.swp.persistence.UserDAO;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Adds password hashing to {@link UserDAO} using the Scrypt library. When
 * inserting a new user ({@link UserDAO#insert(User)}), its password is set to
 * its first name ({@link User#getFirstName()}).
 */
@Decorator
public abstract class UserDAOScryptDecorator implements UserDAO {

    /**
     * Decorated class.
     */
    @Inject
    @Delegate
    @Any
    UserDAO userDAO;

    @Override
    public Optional<User> findByEmailAndPassword(
            final String email, final String password) {
        return findByEmail(email).filter(u ->
                SCryptUtil.check(password, u.getPassword()));
    }

    @Override
    public void insert(final User user) throws NullPointerException,
            UserAlreadyInsertedException, DuplicateEmailException {
        final String pwHash = SCryptUtil.scrypt(
                user.getPassword(),
                (int) Math.pow(2, 15) /* CPU cost parameter */,
                8 /* Memory cost parameter */,
                1 /* Parallelization parameter */);
        user.setPassword(pwHash);
        userDAO.insert(user);
    }
}
