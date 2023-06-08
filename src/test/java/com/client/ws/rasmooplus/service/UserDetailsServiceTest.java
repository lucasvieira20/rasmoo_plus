package com.client.ws.rasmooplus.service;

import com.client.ws.rasmooplus.exception.BadRequestException;
import com.client.ws.rasmooplus.exception.NotFoundException;
import com.client.ws.rasmooplus.integration.MailIntegration;
import com.client.ws.rasmooplus.model.jpa.UserCredentials;
import com.client.ws.rasmooplus.model.jpa.UserType;
import com.client.ws.rasmooplus.model.redis.UserRecoveryCode;
import com.client.ws.rasmooplus.repository.jpa.UserDetailsRepository;
import com.client.ws.rasmooplus.repository.redis.UserRecoveryCodeRepository;
import com.client.ws.rasmooplus.service.implementations.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {
    private static final String USERNAME_ALUNO = "b251e5c711@fireboxmail.lol";
    private static final String PASSWORD_ALUNO = "123546";
    private static final String RECOVERY_CODE_ALUNO = "4065";

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @Mock
    private UserRecoveryCodeRepository userRecoveryCodeRepository;

    @Mock
    private MailIntegration mailIntegration;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void given_loadUserByUsernameAndPass_when_thereIsUsername_then_returnUserCredentials() {
        UserCredentials userCredentials = getUserCredentials();

        Mockito.when(userDetailsRepository.findByUsername(USERNAME_ALUNO)).thenReturn(Optional.of(userCredentials));

        var result = userDetailsService.loadUserByUsernameAndPass(USERNAME_ALUNO, PASSWORD_ALUNO);

        assertEquals(userCredentials.getUsername(), result.getUsername());

        Mockito.verify(userDetailsRepository, Mockito.times(1)).findByUsername(USERNAME_ALUNO);

    }

    @Test
    void given_loadUserByUsernameAndPass_when_thereIsNoUsername_then_throwNotFoundException() {
        Mockito.when(userDetailsRepository.findByUsername(USERNAME_ALUNO)).thenReturn(Optional.empty());
        assertEquals("Usuário não encontrado", assertThrows(NotFoundException.class,
                () -> userDetailsService.loadUserByUsernameAndPass(USERNAME_ALUNO, PASSWORD_ALUNO))
                .getLocalizedMessage());

        Mockito.verify(userDetailsRepository, Mockito.times(1)).findByUsername(USERNAME_ALUNO);
    }

    @Test
    void given_loadUserByUsernameAndPass_when_passIsNotCorrect_then_throwBadRequestException() {
        UserCredentials userCredentials = getUserCredentials();

        Mockito.when(userDetailsRepository.findByUsername(USERNAME_ALUNO)).thenReturn(Optional.of(userCredentials));

        assertEquals("Usuário ou senha inválido",
                assertThrows(BadRequestException.class,
                        () -> userDetailsService.loadUserByUsernameAndPass(USERNAME_ALUNO, "pass"))
                        .getLocalizedMessage());

        Mockito.verify(userDetailsRepository, Mockito.times(1)).findByUsername(USERNAME_ALUNO);
    }

    @Test
    void given_sendRecoveryCode_when_userRecoveryCodeIsFound_then_updateUserAndSendEmail() {
        UserRecoveryCode userRecoveryCode = getUserRecoveryCode();

        Mockito.when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO)).thenReturn(Optional.of(userRecoveryCode));

        userDetailsService.sendRecoveryCode(USERNAME_ALUNO);

        Mockito.verify(userRecoveryCodeRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(mailIntegration, Mockito.times(1)).send(Mockito.any(),Mockito.any(),Mockito.any());

    }
    @Test
    void given_sendRecoveryCode_when_userRecoveryCodeIsNotFound_then_SaveUserAndSendEmail() {
        Mockito.when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO)).thenReturn(Optional.empty());
        Mockito.when(userDetailsRepository.findByUsername(USERNAME_ALUNO)).thenReturn(Optional.of(getUserCredentials()));

        userDetailsService.sendRecoveryCode(USERNAME_ALUNO);

        Mockito.verify(userRecoveryCodeRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(mailIntegration, Mockito.times(1)).send(Mockito.any(), Mockito.any(), Mockito.any());

    }

    @Test
    void given_sendRecoveryCode_when_userRecoveryCodeIsNotFoundAndUserDetailsIsNotFound_then_throwNotFoundException() {
        Mockito.when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO)).thenReturn(Optional.empty());
        Mockito.when(userDetailsRepository.findByUsername(USERNAME_ALUNO)).thenReturn(Optional.empty());

        try {
            userDetailsService.sendRecoveryCode(USERNAME_ALUNO);
        } catch (Exception e ) {
            assertEquals(NotFoundException.class, e.getClass());
            assertEquals("Usuário não encontrado", e.getMessage());
        }

        Mockito.verify(userRecoveryCodeRepository, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(mailIntegration, Mockito.times(0)).send(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void given_recoveryCodeIsValid_when_userIsFound_then_returnTrue () {
        ReflectionTestUtils.setField(userDetailsService, "recoveryCodeTimeout", "5");
        Mockito.when(userRecoveryCodeRepository.findByEmail(USERNAME_ALUNO)).thenReturn(Optional.of(getUserRecoveryCode()));
        assertTrue(userDetailsService.recoveryCodeIsValid(RECOVERY_CODE_ALUNO, USERNAME_ALUNO));

        Mockito.verify(userRecoveryCodeRepository, Mockito.times(1)).findByEmail(USERNAME_ALUNO);
    }

    private static UserRecoveryCode getUserRecoveryCode() {
        UserRecoveryCode userRecoveryCode = new UserRecoveryCode(
                UUID.randomUUID().toString(),
                USERNAME_ALUNO,
                RECOVERY_CODE_ALUNO,
                LocalDateTime.now()
        );
        return userRecoveryCode;
    }

    private UserCredentials getUserCredentials() {
        UserType userType = new UserType(1L, "aluno", "aluno plataforma");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return new UserCredentials(1L, USERNAME_ALUNO, encoder.encode(PASSWORD_ALUNO), userType);
    }
}
