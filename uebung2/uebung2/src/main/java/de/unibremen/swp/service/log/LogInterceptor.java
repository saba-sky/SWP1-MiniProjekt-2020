package de.unibremen.swp.service.log;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

/**
 * The interceptor of {@link Log}.
 */
@Interceptor
@Log
public class LogInterceptor implements Serializable {
    @AroundInvoke
    public Object log(final InvocationContext ctx) throws Exception {
        final String name = ctx.getMethod().getDeclaringClass().getName()
                + "#" + ctx.getMethod().getName();
        System.out.println("enter: " + name);
        final Object result = ctx.proceed();
        System.out.println("exit: " + name);
        return result;
    }
}
