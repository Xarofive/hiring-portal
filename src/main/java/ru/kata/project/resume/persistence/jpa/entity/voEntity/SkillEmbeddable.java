package ru.kata.project.resume.persistence.jpa.entity.voEntity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kata.project.resume.core.entity.vo.Skill;

import java.util.Objects;
import java.util.UUID;
/**
 * JPA-встраиваемая сущность с техническим ID для избежания коллизий
 * <p>Содержит методы для конвертации в доменный объект и обратно.</p>
 */

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class SkillEmbeddable {
    private  UUID id;
    private String name;

    @Enumerated(EnumType.STRING)
    private Skill.SkillLevel level;

    public SkillEmbeddable(String name, Skill.SkillLevel level) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.level = level;
    }

    public static SkillEmbeddable fromDomain(Skill skill) {
        return new SkillEmbeddable(skill.name(), skill.level());
    }

    public Skill toDomain() {
        return new Skill(this.name, this.level);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SkillEmbeddable that = (SkillEmbeddable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}