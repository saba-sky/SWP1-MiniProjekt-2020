package de.unibremen.swp.security;

import de.unibremen.swp.model.Role;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to protect classes and methods from being accessed by users with
 * insufficient authorizations, i.e., only the specified roles (see
 * {@link Role}) are allowed to access a certain method or all methods of a
 * certain class. This annotation is somewhat similar to {@link RolesAllowed},
 * except that is can also be applied on CDI managed beans.
 */
@InterceptorBinding
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Secure {

    /**
     * Roles allowed.
     *
     * @return
     *      Roles allowed.
     */
    @Nonbinding
    Role[] roles() default {};
}
