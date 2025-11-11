package ru.kata.project.user.core.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kata.project.user.core.dto.RegistrationRequestDto;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.entity.UserStatus;
import ru.kata.project.user.core.port.repository.EmailVerificationRepository;
import ru.kata.project.user.core.port.repository.UserRepository;
import ru.kata.project.user.core.port.service.AuthAuditService;
import ru.kata.project.user.core.port.utility.EmailCodeEncoder;
import ru.kata.project.user.core.port.utility.EmailCodeGenerator;
import ru.kata.project.user.core.port.utility.UserPasswordEncoder;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit-тесты для use-case "Регистрация нового пользователя" {@link UserRegistrationUseCase}
 *
 * @author Vladislav_Bogomolov
 */
@ExtendWith(MockitoExtension.class)
public class UserRegistrationUseCaseTest {

    @Mock
    UserRepository userRepository;
    @Mock
    UserPasswordEncoder passwordEncoder;
    @Mock
    EmailVerificationRepository emailVerificationRepository;
    @Mock
    EmailCodeEncoder emailCodeEncoder;
    @Mock
    EmailCodeGenerator emailCodeGenerator;
    @Mock
    AuthAuditService auditService;

    @InjectMocks
    UserRegistrationUseCase useCase;

    private RegistrationRequestDto request;
    private RegistrationRequestDto requestWithoutUserProfile;
    private RegistrationRequestDto requestWithoutLastName;
    private User savedUser;

    @BeforeEach
    void setUp() {
        request = new RegistrationRequestDto(
                "username",
                "mail@example.com",
                "raw_password",
                "Ivan",
                "Ivanov");

        requestWithoutUserProfile = new RegistrationRequestDto(
                "username",
                "mail@example.com",
                "raw_password",
                null,
                null
        );

        requestWithoutLastName = new RegistrationRequestDto(
                "username",
                "mail@example.com",
                "raw_password",
                "Ivan",
                null
        );

        savedUser = User.builder()
                .id(UUID.randomUUID())
                .username("username")
                .email("mail@example.com")
                .passwordHash("password_hash")
                .status(UserStatus.PENDING_EMAIL)
                .roles(Set.of())
                .build();
    }

    @Test
    void givenUniqueUsernameAndEmail_withValidData_whenRegister_thenUserSavedAndEmailVerificationCreatedAndAuditLogged() {
        when(userRepository.existsByEmail("mail@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(passwordEncoder.encode("raw_password")).thenReturn("password_hash");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(emailCodeGenerator.generate()).thenReturn("123456");
        when(emailCodeEncoder.encode("123456")).thenReturn("code_hash");

        final String expected = "Пользователь создан. Пожалуйста, подтвердите почту. Код для подтверждения - 123456";
        final String actual = useCase.execute(request);

        assertEquals(expected, actual);

        verify(passwordEncoder).encode("raw_password");
        verifyNoMoreInteractions(passwordEncoder);
        verify(userRepository).save(argThat(u ->
                u.getUsername().equals("username")
                        && u.getEmail().equals("mail@example.com")
                        && u.getPasswordHash().equals("password_hash")
                        && u.getStatus() == UserStatus.PENDING_EMAIL
                        && u.getUserProfile() != null
        ));
        verify(emailVerificationRepository).save(argThat(ev ->
                ev.getUserId().equals(savedUser.getId())
                        && ev.getCodeHash().equals("code_hash")
        ));
        verify(auditService).logAudit(
                eq(savedUser.getId()),
                eq("REGISTRATION"),
                eq("0.0.0.0:0000"),
                eq("{json:json}"));
    }

    @Test
    void givenUniqueUsernameAndEmail_withoutUserProfile_whenRegister_thenUserSavedAndProfileIsSetAndEmailVerificationCreatedAndAuditLogged() {
        when(userRepository.existsByEmail("mail@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(passwordEncoder.encode("raw_password")).thenReturn("password_hash");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(emailCodeGenerator.generate()).thenReturn("123456");
        when(emailCodeEncoder.encode("123456")).thenReturn("code_hash");

        final String expected = "Пользователь создан. Пожалуйста, подтвердите почту. Код для подтверждения - 123456";
        final String actual = useCase.execute(requestWithoutUserProfile);

        assertEquals(expected, actual);

        verify(passwordEncoder).encode("raw_password");
        verifyNoMoreInteractions(passwordEncoder);
        verify(userRepository).save(argThat(u ->
                u.getUsername().equals("username")
                        && u.getEmail().equals("mail@example.com")
                        && u.getPasswordHash().equals("password_hash")
                        && u.getStatus() == UserStatus.PENDING_EMAIL
                        && u.getUserProfile() == null
        ));
        verify(emailVerificationRepository).save(argThat(ev ->
                ev.getUserId().equals(savedUser.getId())
                        && ev.getCodeHash().equals("code_hash")
        ));
        verify(auditService).logAudit(
                eq(savedUser.getId()),
                eq("REGISTRATION"),
                eq("0.0.0.0:0000"),
                eq("{json:json}"));
    }

    @Test
    void givenUniqueUsernameAndEmail_withoutLastName_whenRegister_thenUserSavedAndProfileIsSetAndEmailVerificationCreatedAndAuditLogged() {
        when(userRepository.existsByEmail("mail@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(passwordEncoder.encode("raw_password")).thenReturn("password_hash");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(emailCodeGenerator.generate()).thenReturn("123456");
        when(emailCodeEncoder.encode("123456")).thenReturn("code_hash");

        final String expected = "Пользователь создан. Пожалуйста, подтвердите почту. Код для подтверждения - 123456";
        final String actual = useCase.execute(requestWithoutLastName);

        assertEquals(expected, actual);

        verify(passwordEncoder).encode("raw_password");
        verifyNoMoreInteractions(passwordEncoder);
        verify(userRepository).save(argThat(u ->
                u.getUsername().equals("username")
                        && u.getEmail().equals("mail@example.com")
                        && u.getPasswordHash().equals("password_hash")
                        && u.getStatus() == UserStatus.PENDING_EMAIL
                        && u.getUserProfile() == null

        ));
        verify(emailVerificationRepository).save(argThat(ev ->
                ev.getUserId().equals(savedUser.getId())
                        && ev.getCodeHash().equals("code_hash")
        ));
        verify(auditService).logAudit(
                eq(savedUser.getId()),
                eq("REGISTRATION"),
                eq("0.0.0.0:0000"),
                eq("{json:json}"));
    }

    @Test
    void givenExistingUsername_whenRegister_thenThrowsException() {
        when(userRepository.existsByUsername("username")).thenReturn(true);

        final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(request));

        assertEquals("Username already exists", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenExistingEmail_whenRegister_thenThrowsException() {
        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(userRepository.existsByEmail("mail@example.com")).thenReturn(true);

        final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(request));

        assertEquals("Email already exists", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenInvalidEmail_whenRegister_thenThrowsException() {
        request = new RegistrationRequestDto(
                "username",
                "invalid-email",
                "raw_password",
                "Ivan",
                "Ivanov"
        );
        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(userRepository.existsByEmail("invalid-email")).thenReturn(false);

        final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(request));

        assertEquals("Bad email address", ex.getMessage());
        verify(userRepository, never()).save(any());
        verify(emailVerificationRepository, never()).save(any());
        verify(auditService, never()).logAudit(any(), any(), any(), any());
    }

    @Test
    void givenRepositoryThrowsException_whenRegister_thenThrowsException() {
        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(userRepository.existsByEmail("mail@example.com")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("password_hash");
        when(userRepository.save(any())).thenThrow(new RuntimeException("DB error"));

        final RuntimeException ex = assertThrows(RuntimeException.class,
                () -> useCase.execute(request));

        assertEquals("DB error", ex.getMessage());
        verify(auditService, never()).logAudit(any(), any(), any(), any());
    }
}