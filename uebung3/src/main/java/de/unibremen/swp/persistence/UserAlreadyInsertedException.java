package de.unibremen.swp.persistence;

import de.unibremen.swp.model.User;

/**
 * Is thrown by {@link UserDAO#insert(User)} to indicate that the given user
 * has already been inserted, i.e., there is another user object in
 * {@link UserDAO} whose id is equal to the id of the user object to insert.
 */
public class UserAlreadyInsertedException extends Exception {
}
