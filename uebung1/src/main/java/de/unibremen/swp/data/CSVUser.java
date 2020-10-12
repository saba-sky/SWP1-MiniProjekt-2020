package de.unibremen.swp.data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.opencsv.bean.AbstractBeanField;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a user imported from a CSV file.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CSVUser {

    /**
     * The identification number of a user.
     */
    @JsonIgnoreProperties
    @CsvBindByName(column = "ID")
    private int id = 1;

    /**
     * A user's first name.
     */
    @CsvBindByName(column = "First Name")
    private String firstName = "";

    /**
     * A user's last name.
     */
    @CsvBindByName(column = "Last Name")
    private String lastName = "";

    /**
     * A user's motto.
     */
    @CsvBindByName(column = "Motto")
    private String motto = "";

    /**
     * Stores the ids of the friends.
     */
    @CsvCustomBindByName(column = "Friends",
            converter = FriendsConverter.class)
    private List<Integer> friends = new ArrayList<>();


    /**
     * Returns the identification number.
     *
     * @return The identification number.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the identification number.
     *
     * @param id The identification number to set.
     */
    public void setId(final int id) {
        this.id = id;
    }

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
     * Returns a flat copy of the friends (their id).
     *
     * @return A flat copy of the id of the friends.
     */

    public List<Integer> getFriends() {
        return new ArrayList<>(friends);
    }

    /**
     * Sets the friends (their id). Creates a flat copy of {@code friends}.
     *
     * @param friends The ids to set.
     * @throws NullPointerException     If {@code friends} is {@code null}.
     * @throws IllegalArgumentException If {@code friends} contains {@code null}.
     */
    public void setFriends(final List<Integer> friends) throws
            NullPointerException, IllegalArgumentException {
        if (Objects.requireNonNull(friends).stream()
                .anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException();
        }
        this.friends = new ArrayList<>(friends);
    }

    public static class FriendsConverter
            extends AbstractBeanField<List<Integer>, String> {
        @Override
        protected Object convert(final String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {

            List<Integer> list = new ArrayList<>();
            String x = value.replaceAll("\\s+", "");
            if (!x.isEmpty()) {
                String[] allStringInTheValue = x.split(",");
                for (String s : allStringInTheValue) {
                    list.add(Integer.parseInt(s));
                }
            }
            return list;
        }

        @Override
        protected String convertToWrite(final Object value)
                throws CsvDataTypeMismatchException {
            @SuppressWarnings("unchecked") List<Integer> values = (List<Integer>) value;
            return values.stream().map(String::valueOf).collect(Collectors.joining(","));
        }

    }

    @Override
    public String toString() {
        return "CSVUser{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", motto='" + motto + '\'' +
                ", friends=" + friends +
                '}';
    }
}

