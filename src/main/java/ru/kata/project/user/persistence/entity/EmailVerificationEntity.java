package ru.kata.project.user.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kata.project.user.core.entity.EmailVerification;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * EmailVerificationEntity
 * <p>
 * Реализация {@link EmailVerification}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Entity
@Table(name = "email_verification")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EmailVerificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private UUID userId;
    private String codeHash;
    private Timestamp consumedAt;
}
