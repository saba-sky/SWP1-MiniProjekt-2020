package de.unibremen.swp.persistence;

import de.unibremen.swp.model.User;

/**
 * Is thrown to indicate that two different users (according to
 * {@link User#equals(Object)} have the same email address
 * ({@link User#getEmail()}).
 */
public class DuplicateEmailException extends Exception {
}
