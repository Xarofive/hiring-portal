package ru.kata.project.persistence.repository.inmemory;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.kata.project.core.entity.Role;
import ru.kata.project.core.port.repository.RoleRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Primary
public class RoleRepositoryInMemory implements RoleRepository {
    private final Map<UUID, Role> storage = new ConcurrentHashMap<>();

    public Role save(Role role) {
        if (role.getId() == null) {
            role.setId(UUID.randomUUID());
        }
        storage.put(role.getId(), role);
        return role;
    }

    @Override
    public Optional<Role> findByCode(String code) {
        return storage.values().stream()
                .filter(r -> r.getCode().equalsIgnoreCase(code))
                .findFirst();
    }

    @Override
    public boolean existsByCode(String code) {
        return storage.values().stream()
                .anyMatch(r -> r.getCode().equalsIgnoreCase(code));
    }

}
