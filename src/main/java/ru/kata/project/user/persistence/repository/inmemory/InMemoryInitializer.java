package ru.kata.project.user.persistence.repository.inmemory;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.project.user.core.entity.Role;
import ru.kata.project.user.core.entity.User;
import ru.kata.project.user.shared.utility.enumeration.UserStatus;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class InMemoryInitializer {

    private final UserRepositoryInMemory userRepository;
    private final RoleRepositoryInMemory roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    private void initAdminUser() {
        Role adminRole = new Role();
        adminRole.setCode("ROLE_ADMIN");
        adminRole.setName("Administrator");
        roleRepository.save(adminRole);
        Role candidateRole = new Role();
        candidateRole.setCode("ROLE_CANDIDATE");
        candidateRole.setName("Candidate");
        roleRepository.save(candidateRole);
        Role recruiterRole = new Role();
        recruiterRole.setCode("ROLE_RECRUITER");
        recruiterRole.setName("Recruiter");
        roleRepository.save(recruiterRole);

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPasswordHash(passwordEncoder.encode("12345"));
        admin.setStatus(UserStatus.ACTIVE);
        admin.setRoles(Set.of(adminRole));

        userRepository.save(admin);
    }
}
