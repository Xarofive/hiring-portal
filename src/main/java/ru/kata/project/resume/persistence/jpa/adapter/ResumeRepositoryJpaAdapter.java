package ru.kata.project.resume.persistence.jpa.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.project.resume.core.entity.Resume;
import ru.kata.project.resume.core.port.ResumeRepository;
import ru.kata.project.resume.persistence.jpa.entity.ResumeEntity;
import ru.kata.project.resume.persistence.jpa.port.ResumeRepositoryJpaPort;
import ru.kata.project.resume.utility.resumeExeption.ResumeDataException;
import ru.kata.project.resume.utility.resumeExeption.ResumeValidationException;
import ru.kata.project.user.core.exception.UserNotFoundException;
import ru.kata.project.user.core.port.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
/**
 * Класс для работы с резюме в базе данных через JPA.
 *
 * <p>Основные функции:
 * <ul>
 *   <li>Сохранение резюме с валидацией уникальности заголовка для пользователя</li>
 *   <li>Поиск резюме по идентификатору</li>
 *   <li>Удаление резюме по идентификатору</li>
 *   <li>Поиск всех резюме пользователя</li>
 * </ul>
 */

@Slf4j
@Repository
@Transactional(readOnly = true)
public class ResumeRepositoryJpaAdapter implements ResumeRepository {

    private final ResumeRepositoryJpaPort resumeRepositoryJpa;
    private final UserRepository userRepository;

    public ResumeRepositoryJpaAdapter(ResumeRepositoryJpaPort resumeRepositoryJpa, UserRepository userRepository) {
        this.resumeRepositoryJpa = resumeRepositoryJpa;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Resume save(Resume resume) {
        final ResumeEntity entity = new ResumeEntity(resume);
        if (resumeRepositoryJpa.existsByUserIdAndTitle(entity.getUserId(), entity.getTitle())) {
            throw new ResumeValidationException("У пользователя не может быть два резюме с одинаковым заголовком");
        }
        try {
            final ResumeEntity savedEntity = resumeRepositoryJpa.save(entity);
            log.info("Сохранено резюме с id = {} userId = {}", savedEntity.getId(), savedEntity.getUserId());
            return savedEntity.toDomain();
        } catch (DataAccessException e) {
            throw new ResumeDataException("Ошибка базы данных при сохранении резюме", entity.getId(), entity.getUserId(), e);
        }
    }

    @Override
    public Optional<Resume> findById(UUID id) {
        return resumeRepositoryJpa.findById(id)
                .map(ResumeEntity::toDomain);
    }

    @Transactional
    @Override
    public boolean deleteById(UUID id) {
        try {
            if (resumeRepositoryJpa.existsById(id)) {
                final Optional<ResumeEntity> resumeEntity =resumeRepositoryJpa.findById(id);
                resumeRepositoryJpa.delete(resumeEntity.get());
                log.info("Удалено резюме с id = {}", id);
                return true;
            }
            log.debug("Резюме не найдено для удаления: {}", id);
            return false;
        } catch (DataAccessException e) {
            throw new ResumeDataException("Ошибка базы данных при удалении резюме " + id, e, id);
        }
    }

    @Override
    public List<Resume> findByUserId(UUID userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь с ID " + userId + " не найден");
        }
        try {
            return resumeRepositoryJpa.findByUserId(userId).stream()
                    .map(ResumeEntity::toDomain)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new ResumeDataException("Ошибка базы данных при поиске всех резюме пользователя с userId = " + userId, userId, e);
        }
    }
}