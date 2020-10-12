package de.unibremen.swp.controller;

import de.unibremen.swp.model.Role;
import de.unibremen.swp.model.User;
import de.unibremen.swp.persistence.UserDAO;
import de.unibremen.swp.security.Secure;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Allows to load and delete users.
 */
@Named
@RequestScoped
@Secure(roles = {Role.STUDENT, Role.LECTURER, Role.TUTOR})
public class UsersBean {

    /**
     * Our user data storage.
     */
    @Inject
    private UserDAO userDAO;

    /**
     * The users loaded from {@link #userDAO}.
     */
    private List<User> users;

    /**
     * Initializes this bean.
     */
    @PostConstruct
    private void init() {
        users = userDAO.allUsers();
    }

    /**
     * Returns all users (no copy).
     *
     * @return All users.
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Delete the given user.
     *
     * @param user User to delete.
     */
    @Secure(roles = {Role.LECTURER})
    public void delete(final User user) {
        userDAO.delete(user);
        init();
    }
}
