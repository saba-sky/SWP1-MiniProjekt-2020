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

@Decorator
public abstract class UserDAOScryptDecorator implements UserDAO {

    @Inject
    @Delegate
    @Any
    UserDAO userDAO;

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        for (int i = 0; i < userDAO.allUsers().size(); i++) {
            if (userDAO.allUsers().get(i).getEmail().equals(email)) {
                if (SCryptUtil.check(password, userDAO.allUsers().get(i).getPassword())) {
                    return Optional.ofNullable(userDAO.allUsers().get(i));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public void insert(User user) throws NullPointerException, UserAlreadyInsertedException, DuplicateEmailException {
        String firstName = user.getFirstName();
        firstName = capitalizeFirstName(firstName);
        System.out.println("gotten user email: " + user.getEmail());
        user.setPassword(SCryptUtil.scrypt(firstName, (int) Math.pow(2, 15), 8, 1));
        userDAO.insert(user);
    }

    private static String capitalizeFirstName(String firstName) {
        if (firstName == null) return null;
        return firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
    }
}