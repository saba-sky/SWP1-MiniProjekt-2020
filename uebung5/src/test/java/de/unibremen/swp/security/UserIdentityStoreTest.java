package de.unibremen.swp.security;

import de.unibremen.swp.model.Role;
import de.unibremen.swp.model.User;
import de.unibremen.swp.persistence.UserDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserIdentityStoreTest {
    @Mock
    private UserDAO userDAO;
    @Mock
    private CredentialValidationResult credentialValidationResult;
    @InjectMocks
    private UserIdentityStore identityStore;

    @Test
    void notValidatedResultTest() {
        Credential credential = mock(Credential.class);
        identityStore.validate(credential);
        verifyNoInteractions(userDAO);
        assertEquals(CredentialValidationResult.NOT_VALIDATED_RESULT, identityStore.validate(credential));

    }

    @Test
    void inValidatedResultTest() {
        UsernamePasswordCredential usernamePasswordCredential = mock(UsernamePasswordCredential.class);
        identityStore.validate(usernamePasswordCredential);
        assertEquals(CredentialValidationResult.INVALID_RESULT, identityStore.validate(usernamePasswordCredential));

    }

    @Test
    void credentialValidationResultStudent() {
        UsernamePasswordCredential usernamePasswordCredential = mock(UsernamePasswordCredential.class);
        User userMock = mock(User.class);
        when(usernamePasswordCredential.getCaller()).thenReturn("saya@uni-bremen.de");
        when(usernamePasswordCredential.getPasswordAsString()).thenReturn("Saba");
        when(userDAO.findByEmailAndPassword(any(), any())).thenReturn(Optional.of(userMock));
        Optional<User> user = userDAO.findByEmailAndPassword(any(), any());
        assertTrue(user.isPresent());
        when(user.get().getEmail()).thenReturn("saya@uni-bremen.de");
        when(user.get().getRole()).thenReturn(Role.STUDENT);
        verifyNoInteractions(credentialValidationResult);
        assertNotNull(identityStore.validate((usernamePasswordCredential)));

    }

    @Test
    void credentialValidationResultTutor() {
        UsernamePasswordCredential usernamePasswordCredential = mock(UsernamePasswordCredential.class);
        User userMock = mock(User.class);
        when(usernamePasswordCredential.getCaller()).thenReturn("s_cychgf@uni-bremen.de");
        when(usernamePasswordCredential.getPasswordAsString()).thenReturn("Marcel");
        when(userDAO.findByEmailAndPassword(any(), any())).thenReturn(Optional.of(userMock));
        Optional<User> user = userDAO.findByEmailAndPassword(any(), any());
        assertTrue(user.isPresent());
        when(user.get().getEmail()).thenReturn("s_cychgf@uni-bremen.de");
        when(user.get().getRole()).thenReturn(Role.TUTOR);
        verifyNoInteractions(credentialValidationResult);
        assertNotNull(identityStore.validate((usernamePasswordCredential)));

    }

    @Test
    void credentialValidationResultLecturer() {
        UsernamePasswordCredential userAndPass = mock(UsernamePasswordCredential.class);
        User userMock = mock(User.class);
        when(userAndPass.getCaller()).thenReturn("hoelsch@uni-bremen.de");
        when(userAndPass.getPasswordAsString()).thenReturn("Karsten");
        when(userDAO.findByEmailAndPassword(any(), any())).thenReturn(Optional.of(userMock));
        Optional<User> user = userDAO.findByEmailAndPassword(any(), any());
        assertTrue(user.isPresent());
        when(user.get().getEmail()).thenReturn("hoelsch@uni-bremen.de");
        when(user.get().getRole()).thenReturn(Role.LECTURER);
        verifyNoInteractions(credentialValidationResult);
        assertNotNull(identityStore.validate((userAndPass)));

    }
}