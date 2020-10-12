package de.unibremen.swp.service.log;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
     * Indicates that the entrance and exit of a method call (or all methods calls
     * of a class) should be logged.
     */
    @InterceptorBinding
    @Target({ METHOD, TYPE })
    @Retention(RUNTIME)
    public @interface Log {
    }

