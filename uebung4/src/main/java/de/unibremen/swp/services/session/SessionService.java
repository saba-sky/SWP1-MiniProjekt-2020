package de.unibremen.swp.services.session;

import de.unibremen.swp.model.User;
import lombok.NonNull;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Stores the active sessions of authenticated users ({@link User}). The inner
 * class ({@link SessionListener}) is used to remove invalidated sessions from
 * {@link #sessions} using {@link #loggedOut(HttpSession)}.
 */
@Singleton
public class SessionService {

    /**
     * Email ({@link User#getEmail()}) to active sessions.
     */
    private final Map<String, Set<HttpSession>> sessions = new HashMap<>();

    /**
     * Adds the given session as active session of the user identified by
     * {@code email}. The attribute 'principle' with value {@code email} is
     * stored in {@code session} for later purpose.
     *
     * @param email
     *      Email address of the user associated with {@code session}.
     * @param session
     *      Active session of user identified by {@code email}.
     * @throws NullPointerException
     *      If any of the given arguments is {@code null}.
     */
    @Lock(LockType.WRITE)
    @NonNull
    public void loggedIn(final String email, final HttpSession session) {
//        Objects.requireNonNull(email);
//        Objects.requireNonNull(session);
        session.setAttribute("principle", email);
        sessions.computeIfAbsent(email, __ -> new HashSet<>()).add(session);
    }

    /**
     * Removes the given session from {@link #sessions}. Does nothing if
     * attribute 'principle' is not stored in {@code session}. This method is
     * used by {@link SessionListener#sessionDestroyed(HttpSessionEvent)} to
     * remove a session after invalidation.
     *
     * @param session
     *      Session to remove.
     * @throws NullPointerException
     *      If {@code session} is {@code null}.
     */
    @Lock(LockType.WRITE)
    @NonNull
    public void loggedOut(final HttpSession session) {
//        Objects.requireNonNull(session);
        final String email = (String) session.getAttribute("principle");
        if (email != null) {
            final Set<HttpSession> sessionsOfUser = sessions.get(email);
            if (sessionsOfUser != null) {
                sessionsOfUser.removeIf(s ->
                        s.getId().equals(session.getId()));
                if (sessionsOfUser.isEmpty()) {
                    sessions.remove(email);
                }
            }
        }
    }

    /**
     * Invalidates and removes all sessions of the user identified by
     * {@code email}.
     *
     * @param email
     *      Email address of the user whose sessions are invalidated and
     *      removed.
     * @throws NullPointerException
     *      If {@code email} is {@code null}.
     */
    @Lock(LockType.WRITE)
    @NonNull
    public void logout(final String email) {
//        Objects.requireNonNull(email);
        final Set<HttpSession> sessionsOfUser = sessions.get(email);
        if (sessionsOfUser != null) {
            // Avoid `ConcurrentModificationException` by creating a flat copy
            // of `sessionsOfUser`. This exception is thrown because
            // invalidating a session triggers `loggedOut` (which in turn
            // modifies the set we are currently iterating through).
            for (final HttpSession session : new ArrayList<>(sessionsOfUser)) {
                try {
                    session.invalidate();
                } catch (final IllegalStateException e) {
                    /* ignored */
                }
            }
        }
    }

    /**
     * Removes invalidated sessions from {@link #sessions} using
     * {@link #loggedOut(HttpSession)}.
     */
    @WebListener
    public static class SessionListener implements HttpSessionListener {

        @Inject
        private SessionService sessionService;

        @Override
        public void sessionDestroyed(final HttpSessionEvent se) {
                sessionService.loggedOut(se.getSession());
        }
    }
}
