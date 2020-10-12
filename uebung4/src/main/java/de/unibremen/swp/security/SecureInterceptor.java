package de.unibremen.swp.security;

import de.unibremen.swp.model.Role;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.security.enterprise.SecurityContext;
import java.io.Serializable;

/**
 * Implements the security checks specified by {@link Secure}.
 */
@Interceptor
@Secure
public class SecureInterceptor implements Serializable {

    /**
     * Used to check a user's role.
     */
    @Inject
    private SecurityContext context;

    @AroundInvoke
    public Object check(final InvocationContext ctx) throws Exception {
        // Annotated methods override annotated classes.
        Secure annotation = ctx.getMethod().getAnnotation(Secure.class);
        if (annotation == null) {
            annotation = ctx.getMethod().getDeclaringClass()
                    .getAnnotation(Secure.class);
        }
        // Just to be sure.
        if (annotation == null) {
            throw new IllegalStateException(
                    "Cannot find annotation 'Secure'");
        }

        for (final Role role : annotation.roles()) {
            if (context.isCallerInRole(role.toString())) {
                return ctx.proceed();
            }
        }
        throw new SecurityException(
                "Principal is not allowed to access method");
    }
}
