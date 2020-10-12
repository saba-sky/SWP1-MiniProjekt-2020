package de.unibremen.swp.model;

import com.opencsv.bean.CsvBindByName;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.UUID;

/**
 * A user with id, first name, last name, and email address. Id and email are
 * unique amongst user objects. A user's email address can be changed, but its
 * id remains the same. Two users, {@code u1} and {@code u2}, are considered
 * equal if their ids are equal: {@code u1.getId().equals(u2.getId())}.
 */
public class User {

    /**
     * A user's id (UUID).
     */
    @CsvBindByName(column = "Id")
    private String id = UUID.randomUUID().toString();

    /**
     * A user's first name.
     */
    @NotBlank
    @CsvBindByName(column = "Vorname")
    private String firstName = "";

    /**
     * A user's last name.
     */
    @NotBlank
    @CsvBindByName(column = "Nachname")
    private String lastName = "";

    /**
     * A user's e-mail address.
     */
    @Email
    @NotBlank
    @CsvBindByName(column = "E-Mail")
    private String email = "";

    /**
     * Default constructor.
     */
    public User() {}

    /**
     * Copy constructor.
     *
     * @param other
     *      User to copy.
     * @throws NullPointerException
     *      If {@code other} is {@code null}.
     */
    public User(final User other) throws NullPointerException {
        Objects.requireNonNull(other);
        this.id = other.getId();
        setFirstName(other.getFirstName());
        setLastName(other.getLastName());
        setEmail(other.getEmail());
    }

    /**
     * Returns the id
     *
     * @return
     *      Id of user.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the first name.
     *
     * @return
     *      First name of user.
     */
    public String   getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName
     *      First name to set.
     * @throws NullPointerException
     *      If {@code firstName} is {@code null}.
     */
    public void setFirstName(String firstName) throws NullPointerException {
        this.firstName = Objects.requireNonNull(firstName);
    }

    /**
     * Returns the last name.
     *
     * @return
     *      Last name of user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last name.
     *
     * @param lastName
     *      Last name to set.
     * @throws NullPointerException
     *      If {@code lastName} is {@code null}.
     */
    public void setLastName(String lastName) throws NullPointerException {
        this.lastName = Objects.requireNonNull(lastName);
    }

    /**
     * Returns the email address.
     *
     * @return
     *      Email address of user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email
     *      Email address to set.
     * @throws NullPointerException
     *      If {@code email} is {@code null}.
     */
    public void setEmail(String email) throws NullPointerException {
        this.email = Objects.requireNonNull(email);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final User other = (User) o;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
