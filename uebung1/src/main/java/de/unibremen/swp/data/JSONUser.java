package de.unibremen.swp.data;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a user imported from a JSON file.
 */
// @JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class JSONUser {
    public JSONUser() {
    }

    public JSONUser(String firstName, String lastName, String motto, List<JSONUser> friends) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.motto = motto;
        this.friends = friends;
    }

    /**
     * A user's first name.
     */
    private String firstName = "";

    /**
     * A user's last name.
     */
    private String lastName = "";

    /**
     * A user's motto.
     */
    private String motto = "";

    /**
     * The friends of an user.
     */
    private List<JSONUser> friends = new ArrayList<>();

    /**
     * Returns the first name.
     *
     * @return The first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName The first name to set.
     * @throws NullPointerException If {@code firstName} is {@code null}.
     */
    public void setFirstName(final String firstName) throws
            NullPointerException {
        this.firstName = Objects.requireNonNull(firstName);
    }

    /**
     * Returns the last name.
     *
     * @return The last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName The last name to set.
     * @throws NullPointerException If {@code lastName} is {@code null}.
     */
    public void setLastName(final String lastName) throws
            NullPointerException {
        this.lastName = Objects.requireNonNull(lastName);
    }

    /**
     * Returns the motto.
     *
     * @return The motto.
     */
    public String getMotto() {
        return motto;
    }

    /**
     * Sets the motto.
     *
     * @param motto The motto to set.
     * @throws NullPointerException If {@code motto} is {@code null}.
     */
    public void setMotto(final String motto) throws NullPointerException {
        this.motto = Objects.requireNonNull(motto);
    }

    /**
     * Returns a flat copy of the friends.
     *
     * @return A flat copy of the friends.
     */
    public List<JSONUser> getFriends() {
        return new ArrayList<>(friends);
    }

    /**
     * Sets the friends. Creates a flat copy of {@code friends}.
     *
     * @param friends The friends to set.
     * @throws NullPointerException     If {@code friends} is {@code null}.
     * @throws IllegalArgumentException If {@code friends} contains {@code null}.
     */
    public void setFriends(final List<JSONUser> friends) throws
            NullPointerException, IllegalArgumentException {
        if (Objects.requireNonNull(friends).stream()
                .anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException();
        }
        this.friends = new ArrayList<>(friends);
    }
}
