package de.unibremen.swp.persistence;

import de.unibremen.swp.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Allows to insert, update, delete, and load {@link User} objects.
 */
public interface UserDAO {

    /**
     * Inserts the given user.
     *
     * @param user User to insert.
     * @throws NullPointerException         If {@code user} is {@code null}.
     * @throws UserAlreadyInsertedException If {@code user} has already been inserted.
     * @throws DuplicateEmailException      If the email of {@code user} is already in use.
     */
    void insert(User user) throws NullPointerException,
            UserAlreadyInsertedException, DuplicateEmailException;

    /**
     * Updates the given user.
     *
     * @param user User to update.
     * @throws NullPointerException    If {@code user} is {@code null}.
     * @throws NoSuchUserException     If {@code user} is unknown.
     * @throws DuplicateEmailException If the email of {@code user} is already in use.
     */
    void update(User user) throws NullPointerException, NoSuchUserException,
            DuplicateEmailException;

    /**
     * Deletes the given user. Does not fail if {@code user} is {@code null}
     * or unknown.
     *
     * @param user User to delete.
     */
    void delete(User user);

    /**
     * Returns all users.
     *
     * @return All users.
     */
    List<User> allUsers();

    /**
     * Returns the user with given id.
     *
     * @param id Id of the user in question.
     * @return User with given id.
     */
    Optional<User> findById(String id);

    /**
     * Returns the user with given email.
     *
     * @param email Email of user in question.
     * @return User with given email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Returns the user with given email and password. The main purpose of this
     * method is to validate user credentials, i.e., if the returned optional
     * is not empty, the supplied email and password are valid.
     *
     * @param email    Email of user in question.
     * @param password Password of user in question.
     * @return User with given email and password.
     */
    Optional<User> findByEmailAndPassword(String email, String password);


}
