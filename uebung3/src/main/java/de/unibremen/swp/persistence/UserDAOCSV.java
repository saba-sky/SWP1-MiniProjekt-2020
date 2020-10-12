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
            .resolve("uebung3")
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
                    .build().stream()
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
    @Lock(LockType.WRITE)
    public void insert(final User user) throws NullPointerException,
            UserAlreadyInsertedException, DuplicateEmailException {
        Objects.requireNonNull(user);
        if (users.contains(user)) {
            throw new UserAlreadyInsertedException();
        } else if (findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateEmailException();
        }
        users.add(new User(user));
        writeCsv();
    }

    @Override
    @Lock(LockType.WRITE)
    public void update(final User user) throws NullPointerException,
            NoSuchUserException, DuplicateEmailException {
        Objects.requireNonNull(user);
        final int idx = users.indexOf(user);
        if (idx < 0) {
            throw new NoSuchUserException();
        }
        final Optional<User> match = findByEmail(user.getEmail());
        if (match.isPresent() && !match.get().equals(user)) {
            throw new DuplicateEmailException();
        }
        users.set(idx, new User(user));
        writeCsv();
    }

    @Override
    @Lock(LockType.WRITE)
    public void delete(final User user) {
        if (user != null) {
            users.remove(user);
            writeCsv();
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
                .findFirst()
                .map(User::new);
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

    @Override
    @Lock(LockType.READ)
    public Optional<User> findByEmailAndPassword(
            final String email, final String password) {
        return findByEmail(email).filter(
                u -> u.getPassword().equals(password));
    }
}
