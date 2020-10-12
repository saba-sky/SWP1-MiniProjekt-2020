package de.unibremen.swp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import de.unibremen.swp.data.CSVUser;
import de.unibremen.swp.data.JSONUser;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Provides methods to read (also referred to as deserialization) and write
 * (also referred to as serialization) {@link CSVUser} and {@link JSONUser}
 * from and to strings.
 */
public class Parser {

    /**
     * The delimiter used to separate values in CSV.
     */
    private static final char CSV_DELIMITER = '‽';

    /**
     * Implements the integrity checks of {@link #readCSVUsers(String)} and
     * {@link #writeCSVUsers(List)}.
     */
    private void validate(final List<CSVUser> users) {
        List<Integer> ids = new ArrayList<>();

        for (CSVUser user : users) {
            ids.add(user.getId());
        }

        for (CSVUser user : users) {
            if (user.getId() < 1 || user.getId() > users.size()) {
                throw new IllegalArgumentException();
            }
        }

        for (int i = 0; i < users.size() - 1; i++) {
            if (users.get(i).getId() == users.get(i + 1).getId()) {
                throw new IllegalArgumentException();
            }

        }

        for (CSVUser user : users) {
            if (!ids.containsAll(user.getFriends()) && !user.getFriends().isEmpty()) {
                throw new IllegalArgumentException();
            }
        }


    }


    /**
     * Parses the given CSV string and creates a list of {@link CSVUser} from
     * it. Duplicates (according to {@link CSVUser#equals(Object)}) are
     * filtered out.
     *
     * @param csv The CSV string to parse.
     * @return The users deserialized from {@code csv}.
     * @throws NullPointerException     If {@code csv} is {@code null}.
     * @throws IllegalArgumentException If i) the id of a user is less than {@code 1} or greater than the
     *                                  total number of users stored in {@code csv}, ii) two users,
     *                                  {@code u1} and {@code u2} with {@code u1 != u2}, share the same id
     *                                  such that {@code u1.getId() == u2.getId()}, or iii) a user
     *                                  references a non-existing id in its list of friends (cf.
     *                                  {@link CSVUser#getFriends()}).
     * @throws ParseException           If an error occurred while parsing {@code csv}.
     */
    public List<CSVUser> readCSVUsers(final String csv) throws
            NullPointerException, IllegalArgumentException, ParseException {
        Objects.requireNonNull(csv, "csv is null");
        final List<CSVUser> users;
        try (StringReader reader = new StringReader(csv)) {
            users = new CsvToBeanBuilder<CSVUser>(reader)
                    .withType(CSVUser.class)
                    .withSeparator(CSV_DELIMITER)
                    .build().stream()
                    .collect(Collectors.toList());
        } catch (final RuntimeException e) {
            // Thrown by opencsv in case of a parse exception.
            throw new ParseException(e.getMessage(), -1 /* not provided */);
        }
        validate(users);
        return users.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Creates a CSV string from the given list of {@link CSVUser}. Duplicates
     * (according to {@link CSVUser#equals(Object)}) and {@code null} values in
     * {@code users} are filtered out.
     *
     * @param users The users to serialize.
     * @return The resulting CSV string.
     * @throws NullPointerException     If {@code users} is {@code null}.
     * @throws IllegalArgumentException If i) the id of a user is less than {@code 1} or greater than the
     *                                  total number of users stored in {@code users}, ii) two users,
     *                                  {@code u1} and {@code u2} with {@code u1 != u2}, share the same id
     *                                  such that {@code u1.getId() == u2.getId()}, or iii) a user
     *                                  references a non-existing id in its list of friends (cf.
     *                                  {@link CSVUser#getFriends()}).
     */
    public String writeCSVUsers(final List<CSVUser> users) throws
            NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(users, "users is null");
        final List<CSVUser> filteredUsers = users.stream()
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        validate(filteredUsers);
        try (StringWriter writer = new StringWriter()) {
            new StatefulBeanToCsvBuilder<CSVUser>(writer)
                    .withSeparator(CSV_DELIMITER)
                    .build()
                    .write(filteredUsers);
            return writer.toString();
        } catch (final IOException e) {
            throw new IllegalStateException(
                    "Internal error: Could not close string writer", e);
        } catch (final CsvRequiredFieldEmptyException e) {
            throw new IllegalStateException(
                    "Internal error: Required field is missing", e);
        } catch (CsvDataTypeMismatchException e) {
            throw new IllegalStateException(
                    "Internal error: Type mismatch", e);
        }
    }

    /**
     * Parses the given JSON string and creates a list of {@link JSONUser} from
     * it. Duplicates (according to {@link JSONUser#equals(Object)}) are
     * filtered out.
     *
     * @param json The JSON string to parse.
     * @return The users deserialized from {@code json}.
     * @throws NullPointerException If {@code json} is {@code null}.
     * @throws ParseException       If an error occurred while parsing {@code json}.
     */
    public List<JSONUser> readJSONUsers(final String json) throws
            NullPointerException, ParseException {
        Objects.requireNonNull(json, "json is null");
        try {
            final JSONUser[] users = new ObjectMapper()
                    .readValue(json, JSONUser[].class);
            return Arrays.stream(users)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (final JsonProcessingException e) {
            throw new ParseException(e.getMessage(),
                    (int) e.getLocation().getCharOffset());
        }
    }

    /**
     * Creates a JSON string from the given list of {@link JSONUser}.
     * Duplicates (according to {@link JSONUser#equals(Object)}) and
     * {@code null} values in {@code users} are filtered out.
     *
     * @param users The users to serialize.
     * @return The resulting JSON string.
     * @throws NullPointerException If {@code users} is {@code null}.
     */
    public String writeJSONUsers(final List<JSONUser> users) throws
            NullPointerException {
        Objects.requireNonNull(users, "users is null");
        final List<JSONUser> filteredUsers = users.stream()
                .filter(Objects::nonNull)
                .distinct()

                .collect(Collectors.toList());
        try {
            return new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(filteredUsers);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                    "Internal error: Jackson failed", e);
        }
    }

}
