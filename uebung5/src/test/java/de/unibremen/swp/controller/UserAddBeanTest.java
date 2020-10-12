package de.unibremen.swp.controller;
 
import de.unibremen.swp.model.User;
import de.unibremen.swp.persistence.DuplicateEmailException;
import de.unibremen.swp.persistence.UserAlreadyInsertedException;
import de.unibremen.swp.persistence.UserDAO;
import net.vidageek.mirror.dsl.Mirror;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserAddBeanTest {
    @Mock
    private FacesContext facesContext;
    @Mock
    private ExternalContext externalContext;
    @InjectMocks
    private UserAddBean userAddBean;
    @Mock
    private UserDAO userDAO;
    @Mock
    private User user;
    @Mock
    UIComponent emailInput;

    @Test
    void addNewTest() throws IOException {

        userAddBean.add();
        verifyNoInteractions(facesContext);
        verify(externalContext, times(1)).redirect("users.xhtml");

    }

    @Test
    void duplicateEmailTest() throws IOException, UserAlreadyInsertedException, DuplicateEmailException {
        doThrow(new DuplicateEmailException()).when(userDAO).insert(any());
        userAddBean.add();
        new Mirror().on(userAddBean).invoke().method("init").withoutArgs();

        verifyNoInteractions(externalContext);


    }

    @Test
    void userExistTest() throws IOException, UserAlreadyInsertedException, DuplicateEmailException {
        doThrow(new UserAlreadyInsertedException()).when(userDAO).insert(any());
        assertThrows(IllegalStateException.class, userAddBean::add);
        verifyNoInteractions(externalContext);
        verifyNoInteractions(facesContext);
    }

}