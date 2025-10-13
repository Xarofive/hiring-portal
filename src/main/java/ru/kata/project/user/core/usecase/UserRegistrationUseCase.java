package ru.kata.project.user.core.usecase;

import lombok.RequiredArgsConstructor;
import ru.kata.project.user.core.entity.EmailVerification;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.core.entity.UserProfile;
import ru.kata.project.user.core.port.EmailCodeEncoder;
import ru.kata.project.user.core.port.EmailCodeGenerator;
import ru.kata.project.user.core.port.UserPasswordEncoder;
import ru.kata.project.user.core.port.repository.EmailVerificationRepository;
import ru.kata.project.user.core.port.repository.UserRepository;
import ru.kata.project.user.shared.dto.RegistrationRequestDto;
import ru.kata.project.user.shared.security.service.AuthAuditService;
import ru.kata.project.user.shared.utility.enumeration.UserStatus;

import java.util.Set;

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

    private final UserRepository userRepository;
    private final UserPasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailCodeEncoder emailCodeEncoder;
    private final EmailCodeGenerator emailCodeGenerator;
    private final AuthAuditService auditService;

    public String execute(RegistrationRequestDto request) {
        User user = createUser(request);
        String emailVerificationToken = createEmailVerification(user);

        auditService.logAudit(user.getId(), "REGISTRATION", "0.0.0.0:0000", "{json:json}");

        return "Пользователь создан. Пожалуйста, подтвердите почту. Код для подтверждения - " + emailVerificationToken;
    }

    private void validateUniqueUser(RegistrationRequestDto request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    private User createUser(RegistrationRequestDto request) {
        validateUniqueUser(request);
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .status(UserStatus.PENDING_EMAIL)
                .roles(Set.of()).build();
        if (request.getFirstName() != null && request.getLastName() != null) {
            user.setUserProfile(UserProfile.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .build());
        }
        return userRepository.save(user);
    }

    private String createEmailVerification(User user) {
        String code = emailCodeGenerator.generate();
        emailVerificationRepository.save(EmailVerification.builder()
                .userId(user.getId())
                .codeHash(emailCodeEncoder.encode(code))
                .build());
        return code;
    }
}
