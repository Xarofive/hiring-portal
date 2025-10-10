package ru.kata.project.core.usecase;

import lombok.RequiredArgsConstructor;
import ru.kata.project.core.dto.RegistrationRequestCoreDto;
import ru.kata.project.core.entity.EmailVerification;
import ru.kata.project.core.entity.User;
import ru.kata.project.core.entity.UserProfile;
import ru.kata.project.core.port.EmailCodeEncoder;
import ru.kata.project.core.port.EmailCodeGenerator;
import ru.kata.project.core.port.UserPasswordEncoder;
import ru.kata.project.core.port.repository.EmailVerificationRepository;
import ru.kata.project.core.port.repository.UserRepository;
import ru.kata.project.shared.security.service.AuthAuditService;
import ru.kata.project.shared.utility.enumeration.UserStatus;

import java.util.Set;

@RequiredArgsConstructor
public class UserRegistrationUseCase {

    private final UserRepository userRepository;
    private final UserPasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailCodeEncoder emailCodeEncoder;
    private final EmailCodeGenerator emailCodeGenerator;
    private final AuthAuditService auditService;

    public String execute(RegistrationRequestCoreDto request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.PENDING_EMAIL);
        user.setRoles(Set.of());

        if (!(request.getFirstName() == null) || !(request.getLastName() == null)) {
            UserProfile userProfile = new UserProfile();
            userProfile.setFirstName(request.getFirstName());
            userProfile.setLastName(request.getLastName());
            user.setUserProfile(userProfile);
        }

        userRepository.save(user);

        String emailVerificationToken = emailCodeGenerator.generate();
        emailVerificationRepository.save(EmailVerification.builder()
                .userId(user.getId())
                .codeHash(emailCodeEncoder.encode(emailVerificationToken))
                .build());

        auditService.logAudit(user.getId(), "REGISTRATION", "0.0.0.0:0000", "{json:json}");

        return "Пользователь создан. Пожалуйста, подтвердите почту. Код для подтверждения - " + emailVerificationToken;
    }
}
