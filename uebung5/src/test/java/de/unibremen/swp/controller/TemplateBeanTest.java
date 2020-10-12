package de.unibremen.swp.controller;

import de.unibremen.swp.model.User;
import de.unibremen.swp.persistence.UserDAO;
import net.vidageek.mirror.dsl.Mirror;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemplateBeanTest {
    @Mock
    Principal principal;
    @Mock
    private User user;
    @Mock
    UserDAO userDAO;
    @Mock
    private HttpSession session;
    @Mock
    private ExternalContext externalContext;
    @InjectMocks
    private TemplateBean templateBean;

    @Test
    public void givenInputExistingUserThenCallInit() throws NoSuchMethodException, InvocationTargetException
            , IllegalAccessException {
        when(userDAO.findByEmail("saya@uni-bremen.de")).thenReturn(Optional.of(user));
        when(principal.getName()).thenReturn("saya@uni-bremen.de");
        new Mirror().on(templateBean).invoke().method("init").withoutArgs();
        verify(userDAO, times(1)).findByEmail("saya@uni-bremen.de");

    }

    @Test
    void getFullNameTest() {
        when(user.getFirstName()).thenReturn("Saba");
        when(user.getLastName()).thenReturn("Yaghoubi");
        templateBean.getFullName();

    }

    @Test
    void logout() throws IOException {
        templateBean.logout();
        verify(session).invalidate();
        verify(externalContext, times(1)).redirect("login.xhtml");
    }
}
