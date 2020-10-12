package de.unibremen.swp.services.session;

import de.unibremen.swp.model.User;
import de.unibremen.swp.persistence.DuplicateEmailException;
import de.unibremen.swp.persistence.NoSuchUserException;
import de.unibremen.swp.persistence.UserDAO;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Decorates {@link UserDAO} to log out a user if its email address or role was
 * changed. This is necessary because Java EE Security API doesn't provide a
 * portable way to update a principle's name or its roles/groups. Also logs out
 * a user after it was deleted.
 */
@Decorator
public abstract class SessionInvalidationDecorator implements UserDAO {

    /**
     * Used to log out a user ({@link SessionService#logout(String)}) after
     * its email address or role was changed ({@link UserDAO#update(User)}) or
     * after it was deleted ({@link UserDAO#delete(User)}) .
     */
    @Inject
    private SessionService sessionService;

    /**
     * Decorated class.
     */
    @Inject
    @Delegate
    @Any
    private UserDAO userDAO;

    @Override
    public void update(final User user) throws NullPointerException,
            NoSuchUserException, DuplicateEmailException {
        final Optional<User> old = userDAO.findById(user.getId());
        userDAO.update(user);
        if (old.isPresent()) {
            final User oldUser = old.get();
            if (!user.getEmail().equals(oldUser.getEmail()) ||
                    !user.getRole().equals(oldUser.getRole())) {
                sessionService.logout(oldUser.getEmail());
            }
        }
    }

    @Override
    public void delete(final User user) {
        if (user != null) {
            userDAO.delete(user);
            sessionService.logout(user.getEmail());
        }
    }
}
