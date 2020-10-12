package de.unibremen.swp.security;

import de.unibremen.swp.controller.UserAddBean;
import de.unibremen.swp.controller.UserEditBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.interceptor.InvocationContext;
import javax.security.enterprise.SecurityContext;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecureInterceptorTest {
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private SecureInterceptor secureInterceptor;

    @Test
    void checkWithAddBean() throws Exception {
        InvocationContext ctx = mock(InvocationContext.class);
        Method method = UserAddBean.class.getMethod("add", null);
        when(ctx.getMethod()).thenReturn(method);
        assertThrows(SecurityException.class,
                new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        secureInterceptor.check(ctx);
                    }
                });

    }

    @Test
    void checkWithEditBean() throws Exception {
        InvocationContext ctx = mock(InvocationContext.class);
        Method method = UserEditBean.class.getMethod("update", null);
        when(ctx.getMethod()).thenReturn(method);
        assertThrows(SecurityException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                secureInterceptor.check(ctx);
            }
        });

    }


}

