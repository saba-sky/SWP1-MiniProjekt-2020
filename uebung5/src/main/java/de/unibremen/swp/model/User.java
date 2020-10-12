package de.unibremen.swp.model;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

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
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
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
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Include
    private String id = UUID.randomUUID().toString();

    /**
     * First name of  user.
     */
    @CsvBindByName(column = "Vorname")
    @NotBlank
    @NonNull
    private String firstName = "";

    /**
     * Last name of user.
     */
    @CsvBindByName(column = "Nachname")
    @NotBlank
    @NonNull
    private String lastName = "";

    /**
     * E-mail address of user.
     */
    @CsvBindByName(column = "E-Mail")
    @Email
    @NotBlank
    @NonNull
    private String email = "";

    /**
     * Role of user.
     */
    @CsvCustomBindByName(column = "Rolle",
            converter = RoleConverter.class)
    @NotNull
    @NonNull
    private Role role = Role.STUDENT;

    /**
     * Password of user.
     */
    @CsvBindByName(column = "Passwort")
    @NotBlank
    @NonNull
    private String password = "";

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
        this.firstName = other.getFirstName();
        this.lastName = other.getLastName();
        this.email = other.getEmail();
        this.role = other.getRole();
        this.password = other.getPassword();
    }
}
