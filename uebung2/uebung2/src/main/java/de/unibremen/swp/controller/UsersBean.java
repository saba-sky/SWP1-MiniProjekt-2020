package de.unibremen.swp.controller;

import de.unibremen.swp.model.User;
import de.unibremen.swp.persistence.UserDAO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
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


    public void delete(User user) {
        userDAO.delete(user);
        init();
    }



}