package de.unibremen.swp.model;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

/**
 * A user with id, first name, last name, email address, role (see
 * {@link Role}), and password. Id and email are unique amongst user objects. A
 * user's email address can be changed, but its id remains the same. Two users,
 * {@code u1} and {@code u2}, are considered equal if their ids are equal:
 * {@code u1.getId().equals(u2.getId())}. The default role is
 * {@link Role#STUDENT}.
 */
public class User {

    /**
     * Converts the "Role" column to {@link Role}. The default implementation
     * of {@link AbstractBeanField#convertToWrite(Object)} converts enums as
     * expected and, thus, does not have to be overridden.
     */
    public static class RoleConverter
            extends AbstractBeanField<Role, String> {
        @Override
        protected Object convert(final String value)
                throws CsvDataTypeMismatchException {
            try {
                return Role.valueOf(value);
            } catch (final IllegalArgumentException e) {
                throw new CsvDataTypeMismatchException(e.getMessage());
            }
        }
    }


    /**
     * Id of user (UUID).
     */
    @CsvBindByName(column = "Id")
    private String id = UUID.randomUUID().toString();

    /**
     * First name of  user.
     */
    @CsvBindByName(column = "Vorname")
    @NotBlank
    private String firstName = "";

    /**
     * Last name of user.
     */
    @CsvBindByName(column = "Nachname")
    @NotBlank
    private String lastName = "";

    /**
     * E-mail address of user.
     */
    @CsvBindByName(column = "E-Mail")
    @Email
    @NotBlank
    private String email = "";

    /**
     * Role of user.
     */
    @CsvCustomBindByName(column = "Rolle",
            converter = RoleConverter.class)
    @NotNull
    private Role role = Role.STUDENT;

    /**
     * Password of user.
     */
    @CsvBindByName(column = "Passwort")
    @NotBlank
    private String password = "";

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Copy constructor.
     *
     * @param other User to copy.
     * @throws NullPointerException If {@code other} is {@code null}.
     */
    public User(final User other) throws NullPointerException {
        Objects.requireNonNull(other);
        this.id = other.getId();
        this.firstName = other.getFirstName();
        this.lastName = other.getLastName();
        this.email = other.getEmail();
        this.role = other.getRole();
        this.password = other.getPassword();
    }

    /**
     * Returns the id
     *
     * @return Id of user.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the first name.
     *
     * @return First name of user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName First name to set.
     * @throws NullPointerException If {@code firstName} is {@code null}.
     */
    public void setFirstName(String firstName) throws NullPointerException {
        this.firstName = Objects.requireNonNull(firstName);
    }

    /**
     * Returns the last name.
     *
     * @return Last name of user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the last name.
     *
     * @param lastName Last name to set.
     * @throws NullPointerException If {@code lastName} is {@code null}.
     */
    public void setLastName(String lastName) throws NullPointerException {
        this.lastName = Objects.requireNonNull(lastName);
    }

    /**
     * Returns the email address.
     *
     * @return Email address of user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email Email address to set.
     * @throws NullPointerException If {@code email} is {@code null}.
     */
    public void setEmail(String email) throws NullPointerException {
        this.email = Objects.requireNonNull(email);
    }

    /**
     * Returns the role.
     *
     * @return Role of user.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets the role.
     *
     * @param role Role to set.
     * @throws NullPointerException If {@code role} is {@code null}.
     */
    public void setRole(final Role role) throws NullPointerException {
        this.role = Objects.requireNonNull(role);
    }

    /**
     * Returns the password.
     *
     * @return Password of user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password Password to set.
     * @throws NullPointerException If {@code password} is {@code null}.
     */
    public void setPassword(final String password)
            throws NullPointerException {
        this.password = Objects.requireNonNull(password);
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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", password='" + password + '\'' +
                '}';
    }
}
