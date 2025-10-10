package ru.kata.project.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kata.project.core.port.Authenticator;
import ru.kata.project.core.port.EmailCodeEncoder;
import ru.kata.project.core.port.EmailCodeGenerator;
import ru.kata.project.core.port.UserPasswordEncoder;
import ru.kata.project.core.port.repository.EmailVerificationRepository;
import ru.kata.project.core.port.repository.TokenRepository;
import ru.kata.project.core.port.repository.UserRepository;
import ru.kata.project.core.usecase.AuthenticateUserUseCase;
import ru.kata.project.core.usecase.ForgotPasswordUseCase;
import ru.kata.project.core.usecase.LogoutUserUseCase;
import ru.kata.project.core.usecase.RefreshTokenUseCase;
import ru.kata.project.core.usecase.ResetPasswordUseCase;
import ru.kata.project.core.usecase.UserRegistrationUseCase;
import ru.kata.project.core.usecase.VerifyEmailUseCase;
import ru.kata.project.shared.security.service.AuthAuditService;
import ru.kata.project.shared.security.service.JwtService;

@Configuration
public class UseCaseBeanConfig {

    @Bean
    public UserRegistrationUseCase userRegistrationUseCase(
            UserRepository userRepository,
            UserPasswordEncoder passwordEncoder,
            EmailVerificationRepository emailVerificationRepository,
            EmailCodeEncoder emailCodeEncoder,
            EmailCodeGenerator emailCodeGenerator,
            AuthAuditService auditService) {

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
                                                 AuthAuditService auditService) {

        return new VerifyEmailUseCase(userRepository,
                emailVerificationRepository,
                emailCodeEncoder,
                auditService);
    }

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(Authenticator authenticationManager,
                                                           UserRepository userRepository,
                                                           JwtService jwtService,
                                                           AuthAuditService auditService) {

        return new AuthenticateUserUseCase(authenticationManager,
                userRepository,
                jwtService,
                auditService);
    }

    @Bean
    public RefreshTokenUseCase refreshTokenUseCase(JwtService jwtService,
                                                   UserRepository userRepository,
                                                   TokenRepository tokenRepository,
                                                   AuthAuditService auditService) {

        return new RefreshTokenUseCase(jwtService,
                userRepository,
                tokenRepository,
                auditService);
    }

    @Bean
    public LogoutUserUseCase logoutUserUseCase(TokenRepository tokenRepository,
                                               JwtService jwtService,
                                               AuthAuditService auditService) {

        return new LogoutUserUseCase(tokenRepository,
                jwtService,
                auditService);
    }

    @Bean
    public ForgotPasswordUseCase forgotPasswordUseCase(UserRepository userRepository,
                                                       EmailVerificationRepository emailVerificationRepository,
                                                       TokenRepository tokenRepository,
                                                       EmailCodeGenerator emailCodeGenerator,
                                                       EmailCodeEncoder emailCodeEncoder,
                                                       JwtService jwtService,
                                                       AuthAuditService auditService) {

        return new ForgotPasswordUseCase(userRepository,
                emailVerificationRepository,
                tokenRepository,
                emailCodeGenerator,
                emailCodeEncoder,
                jwtService,
                auditService);
    }

    @Bean
    public ResetPasswordUseCase resetPasswordUseCase(UserRepository userRepository,
                                                     EmailVerificationRepository emailVerificationRepository,
                                                     UserPasswordEncoder passwordEncoder,
                                                     EmailCodeEncoder emailCodeEncoder,
                                                     AuthAuditService auditService) {

        return new ResetPasswordUseCase(userRepository,
                emailVerificationRepository,
                passwordEncoder,
                emailCodeEncoder,
                auditService);
    }

}