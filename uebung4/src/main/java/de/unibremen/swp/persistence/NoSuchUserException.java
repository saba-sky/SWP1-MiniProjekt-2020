package de.unibremen.swp.persistence;

import de.unibremen.swp.model.User;

/**
 * Is thrown by {@link UserDAO#update(User)} to indicate that the given user
 * has not been inserted (see {@link UserDAO#insert(User)}) yet, i.e., there is
 * no user object in {@link UserDAO} whose id is equal to the id of the user
 * object to update.
 */
public class NoSuchUserException extends Exception {
}
