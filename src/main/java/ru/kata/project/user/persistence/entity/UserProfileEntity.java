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
import ru.kata.project.user.core.entity.UserProfile;

import java.util.UUID;

/**
 * UserProfileEntity
 * <p>
 * Реализация {@link UserProfile}
 * </p>
 *
 * @author Vladislav_Bogomolov
 */
@Entity
@Table(name = "user_profiles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String firstName;
    private String lastName;
    private String locale;
    private String timeZone;
    private String phone;
    private String headline;
    private String avatarUrl;
}
