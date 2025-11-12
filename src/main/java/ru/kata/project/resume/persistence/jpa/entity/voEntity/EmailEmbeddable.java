package ru.kata.project.resume.persistence.jpa.entity.voEntity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kata.project.resume.core.entity.vo.Email;

import java.util.Objects;
/**
 * JPA-встраиваемая сущность для хранения email в базе данных.
 * <p>Содержит методы для конвертации в доменный объект и обратно.</p>
 */

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailEmbeddable {
    private String value;

    public static EmailEmbeddable fromDomain(Email email) {
        return new EmailEmbeddable(email.value());
    }

    public Email toDomain() {
        return new Email(this.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmailEmbeddable that = (EmailEmbeddable) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}