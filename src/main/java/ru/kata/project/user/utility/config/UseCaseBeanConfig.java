package ru.kata.project.user.utility.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kata.project.security.service.JwtServiceAdapter;
import ru.kata.project.user.core.port.repository.EmailVerificationRepository;
import ru.kata.project.user.core.port.repository.TokenRepository;
import ru.kata.project.user.core.port.repository.UserRepository;
import ru.kata.project.user.core.port.service.AuthenticationService;
import ru.kata.project.user.core.port.utility.EmailCodeEncoder;
import ru.kata.project.user.core.port.utility.EmailCodeGenerator;
import ru.kata.project.user.core.port.utility.UserPasswordEncoder;
import ru.kata.project.user.core.usecase.AuthenticateUserUseCase;
import ru.kata.project.user.core.usecase.ForgotPasswordUseCase;
import ru.kata.project.user.core.usecase.LogoutUserUseCase;
import ru.kata.project.user.core.usecase.RefreshTokenUseCase;
import ru.kata.project.user.core.usecase.ResetPasswordUseCase;
import ru.kata.project.user.core.usecase.UserRegistrationUseCase;
import ru.kata.project.user.core.usecase.VerifyEmailUseCase;
import ru.kata.project.user.persistence.service.AuthAuditServiceAdapter;

/**
 * UseCaseBeanConfig
 * <p>
 * Конфигурационный класс для регистрации бинов use-case.
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Configuration
public class UseCaseBeanConfig {

    @Bean
    public UserRegistrationUseCase userRegistrationUseCase(
            UserRepository userRepository,
            UserPasswordEncoder passwordEncoder,
            EmailVerificationRepository emailVerificationRepository,
            EmailCodeEncoder emailCodeEncoder,
            EmailCodeGenerator emailCodeGenerator,
            AuthAuditServiceAdapter auditService) {

        return new UserRegistrationUseCase(
                userRepository,
                passwordEncoder,
                emailVerificationRepository,
                emailCodeEncoder,
                emailCodeGenerator,
                auditService
        );
    }

    @Bean
    public VerifyEmailUseCase verifyEmailUseCase(UserRepository userRepository,
                                                 EmailVerificationRepository emailVerificationRepository,
                                                 EmailCodeEncoder emailCodeEncoder,
                                                 AuthAuditServiceAdapter auditService) {

        return new VerifyEmailUseCase(userRepository,
                emailVerificationRepository,
                emailCodeEncoder,
                auditService);
    }

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(AuthenticationService authenticationManager,
                                                           UserRepository userRepository,
                                                           JwtServiceAdapter jwtServiceAdapter,
                                                           AuthAuditServiceAdapter auditService) {

        return new AuthenticateUserUseCase(authenticationManager,
                userRepository,
                jwtServiceAdapter,
                auditService);
    }

    @Bean
    public RefreshTokenUseCase refreshTokenUseCase(JwtServiceAdapter jwtServiceAdapter,
                                                   UserRepository userRepository,
                                                   TokenRepository tokenRepository,
                                                   AuthAuditServiceAdapter auditService) {

        return new RefreshTokenUseCase(jwtServiceAdapter,
                userRepository,
                tokenRepository,
                auditService);
    }

    @Bean
    public LogoutUserUseCase logoutUserUseCase(TokenRepository tokenRepository,
                                               JwtServiceAdapter jwtServiceAdapter,
                                               AuthAuditServiceAdapter auditService) {

        return new LogoutUserUseCase(tokenRepository,
                jwtServiceAdapter,
                auditService);
    }

    @Bean
    public ForgotPasswordUseCase forgotPasswordUseCase(UserRepository userRepository,
                                                       EmailVerificationRepository emailVerificationRepository,
                                                       TokenRepository tokenRepository,
                                                       EmailCodeGenerator emailCodeGenerator,
                                                       EmailCodeEncoder emailCodeEncoder,
                                                       JwtServiceAdapter jwtServiceAdapter,
                                                       AuthAuditServiceAdapter auditService) {

        return new ForgotPasswordUseCase(userRepository,
                emailVerificationRepository,
                tokenRepository,
                emailCodeGenerator,
                emailCodeEncoder,
                jwtServiceAdapter,
                auditService);
    }

    @Bean
    public ResetPasswordUseCase resetPasswordUseCase(UserRepository userRepository,
                                                     EmailVerificationRepository emailVerificationRepository,
                                                     UserPasswordEncoder passwordEncoder,
                                                     EmailCodeEncoder emailCodeEncoder,
                                                     AuthAuditServiceAdapter auditService) {

        return new ResetPasswordUseCase(userRepository,
                emailVerificationRepository,
                passwordEncoder,
                emailCodeEncoder,
                auditService);
    }
}