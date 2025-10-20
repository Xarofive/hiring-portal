package ru.kata.project.user.core.usecase;

import lombok.RequiredArgsConstructor;
import ru.kata.project.user.core.dto.RegistrationRequestDto;
import ru.kata.project.user.core.entity.EmailVerification;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.entity.UserProfile;
import ru.kata.project.user.core.entity.UserStatus;
import ru.kata.project.user.core.port.repository.EmailVerificationRepository;
import ru.kata.project.user.core.port.repository.UserRepository;
import ru.kata.project.user.core.port.service.AuthAuditService;
import ru.kata.project.user.core.port.utility.EmailCodeEncoder;
import ru.kata.project.user.core.port.utility.EmailCodeGenerator;
import ru.kata.project.user.core.port.utility.UserPasswordEncoder;

import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UserRegistrationUseCase
 * <p>
 * Use-case для обработки сценария "Регистрация нового пользователя".
 * </p>
 * <p>
 * Отвечает за регистрацию нового пользователя
 * и генерацию проверочного кода для подтверждения регистрации пользователя
 * </p>
 * <ul>
 *  <li> сохранение данных нового пользователя через {@link UserRepository};</li>
 *  <li> хеширование пароля через {@link UserPasswordEncoder};</li>
 *  <li> сохранение верификационного кода через {@link EmailVerificationRepository};</li>
 *  <li> хеширование верификационного кода через {@link EmailCodeEncoder};</li>
 *  <li> генерация верификационного кода через {@link EmailCodeGenerator};</li>
 *  <li> логирование события "REGISTRATION" через {@link AuthAuditService}.</li>
 * </ul>
 *
 * @author Vladislav_Bogomolov
 */
@RequiredArgsConstructor
public class UserRegistrationUseCase {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private final UserRepository userRepository;
    private final UserPasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailCodeEncoder emailCodeEncoder;
    private final EmailCodeGenerator emailCodeGenerator;
    private final AuthAuditService auditService;

    public String execute(RegistrationRequestDto request) {
        final User user = createUser(request);
        final String emailVerificationToken = createEmailVerification(user);

        auditService.logAudit(user.getId(), "REGISTRATION", "0.0.0.0:0000", "{json:json}");

        return "Пользователь создан. Пожалуйста, подтвердите почту. Код для подтверждения - " + emailVerificationToken;
    }

    private void validateUniqueUser(RegistrationRequestDto request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    private void validateCorrectEmail(String email) {
        final Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Bad email address");
        }
    }

    private User createUser(RegistrationRequestDto request) {
        validateUniqueUser(request);
        validateCorrectEmail(request.email());
        final User user = User.builder()
                .username(request.username())
                .email(request.email().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.password()))
                .status(UserStatus.PENDING_EMAIL)
                .roles(Set.of()).build();
        if (request.firstName() != null && request.lastName() != null) {
            user.setUserProfile(UserProfile.builder()
                    .firstName(Optional.of(request.firstName()).orElse(""))
                    .lastName(Optional.of(request.lastName()).orElse(""))
                    .build());
        }
        return userRepository.save(user);
    }

    private String createEmailVerification(User user) {
        final String code = emailCodeGenerator.generate();
        emailVerificationRepository.save(EmailVerification.builder()
                .userId(user.getId())
                .codeHash(emailCodeEncoder.encode(code))
                .build());
        return code;
    }
}
