package de.unibremen.swp.persistence;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import de.unibremen.swp.model.User;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Implements {@link UserDAO} using a csv file (see {@link #CSV_FILE}). In
 * order to maintain internal consistency, all non-private methods create deep
 * copies of their parameters and return values.
 */
@Singleton
@Startup
public class UserDAOCSV implements UserDAO {

    /**
     * Delimiter used to separate values in csv.
     */
    private static final char CSV_DELIMITER = ';';

    /**
     * Path to the csv "database".
     */
    private static final Path CSV_FILE = Paths
            .get(System.getProperty("user.home"))
            .resolve(".swp")
            .resolve("uebung2")
            .resolve("users.csv");

    /**
     * List of managed users.
     */
    private List<User> users;

    /**
     * Imports the users from {@link #CSV_FILE}.
     */
    @PostConstruct
    private void init() {
        if (!Files.exists(CSV_FILE)) {
            try (InputStream in = getClass()
                    .getResourceAsStream("/users.csv")) {
                Files.createDirectories(CSV_FILE.getParent());
                Files.copy(in, CSV_FILE);
            } catch (final IOException e) {
                throw new Error("Could not copy csv to: "
                        + CSV_FILE.toString(), e);
            }
        }
        try (Reader reader = new FileReader(CSV_FILE.toFile(), UTF_8)) {
            users = new CsvToBeanBuilder<User>(reader)
                    .withType(User.class)
                    .withSeparator(CSV_DELIMITER)
                    //#TODO change it after then
                    .build().parse().stream()
                    .collect(Collectors.toList());
        } catch (final Exception e) {
            throw new Error("Could not load csv file: "
                    + CSV_FILE.toString(), e);
        }
    }

    /**
     * Writes the users to {@link #CSV_FILE}.
     */
    private void writeCsv() {
        try (Writer writer = new FileWriter(CSV_FILE.toFile(), UTF_8)) {
            new StatefulBeanToCsvBuilder<User>(writer)
                    .withSeparator(CSV_DELIMITER)
                    .build()
                    .write(users);
            writer.flush();
        } catch (final Exception e) {
            throw new IllegalStateException("Could not write csv file: "
                    + CSV_FILE.toString(), e);
        }
    }

    @Override
    @Lock(LockType.READ)
    public List<User> allUsers() {
        return users.stream()
                .map(User::new)
                .collect(Collectors.toList());
    }

    @Override
    @Lock(LockType.READ)
    public Optional<User> findById(final String id) {
        return users.stream()
                // String#equals handles null as false.
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    @Lock(LockType.READ)
    public Optional<User> findByEmail(final String email) {
        return users.stream()
                // String#equals handles null as false.
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .map(User::new);
    }
    //#TODO

    @Lock(LockType.READ)
    public Optional<User> findByFirstName(final String firstName) {
        return users.stream()
                // String#equals handles null as false.
                .filter(u -> u.getFirstName().equals(firstName))
                .findFirst()
                .map(User::new);
    }
    @Lock(LockType.READ)
    public Optional<User> findByLastName(final String lastName) {
        return users.stream()
                // String#equals handles null as false.
                .filter(u -> u.getLastName().equals(lastName))
                .findFirst()
                .map(User::new);
    }

    @Override
    @Lock(LockType.WRITE)
    public void save(final User user) throws DuplicateEmailException {
        Objects.requireNonNull(user);
        final Optional<User> match = findByEmail(user.getEmail());
        if (match.isPresent() && !match.get().equals(user)) {
            throw new DuplicateEmailException();
        }
        final User copy = new User(user);
        final int idx = users.indexOf(copy);
        if (idx >= 0) {
            users.set(idx, copy);
        } else {
            users.add(copy);
        }
        writeCsv();
    }

    @Override
    @Lock(LockType.WRITE)
    public void delete(final User user) {
        if (user != null) {
            users.removeIf(u -> u.getId().equals(user.getId()));
            writeCsv();
        }
    }
}
