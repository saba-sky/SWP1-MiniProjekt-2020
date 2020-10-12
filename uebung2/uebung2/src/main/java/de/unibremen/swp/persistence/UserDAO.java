package de.unibremen.swp.persistence;

import de.unibremen.swp.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Allows to load, save (also referred to as persist), and delete {@link User}
 * objects.
 */

public interface UserDAO {

    /**
     * Returns all users.
     *
     * @return
     *      All users.
     */
    List<User> allUsers();

    /**
     * Returns the user with given id.
     *
     * @param id
     *      Id of the user in question.
     * @return
     *      User with given id.
     */
    Optional<User> findById(String id);

    /**
     * Returns the user with given email.
     *
     * @param email
     *      Email of user in question.
     * @return
     *      User with given email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Saves or updates the given user.
     *
     * @param user
     *      User to save or update.
     * @throws NullPointerException
     *      If {@code user} is {@code null}.
     * @throws DuplicateEmailException
     *      If there is a another user (i.e. a different user according to
     *      {@link User#equals(Object)}) with same email address.
     */
    //Data Access Object
    void save(User user) throws NullPointerException, DuplicateEmailException;

    /**
     * Deletes the given user. Does not fail if {@code user} is {@code null}
     * or unknown.
     *
     * @param user
     *      User to delete.
     */
    void delete( User user);
}
