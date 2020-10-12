package de.unibremen.swp.security;

import de.unibremen.swp.model.Role;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.security.enterprise.SecurityContext;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;


@Interceptor
@Secure(roles = {})
public class SecureInterceptor implements Serializable {

    @Inject
    SecurityContext securityContext;

    @AroundInvoke
    public Object roles(final InvocationContext ctx) throws Exception {
        Method method = ctx.getMethod();
        Class<?> clazz = method.getDeclaringClass();

        boolean accessible = true;

        if (clazz.isAnnotationPresent(Secure.class)) {
            System.out.println("clazz annotation class.");
            Role[] roles = clazz.getAnnotation(Secure.class).roles();
            accessible = Arrays.stream(roles).anyMatch(s -> securityContext.isCallerInRole(s.name()));
            System.out.println("Roles in clazz are: " + Arrays.toString(roles));
            System.out.println("isCallerInRole in class: " + accessible);
        }

        if (method.isAnnotationPresent(Secure.class)) {
            System.out.println("method annotation class.");
            Role[] roles = method.getAnnotation(Secure.class).roles();
            accessible = Arrays.stream(roles).anyMatch(s -> securityContext.isCallerInRole(s.name()));
                }

        if (accessible) {
            return ctx.proceed();
        }
        throw new SecurityException("Custom Security Exception");



    }
}