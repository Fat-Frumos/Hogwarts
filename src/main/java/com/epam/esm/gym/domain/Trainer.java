package com.epam.esm.gym.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a trainer responsible for conducting training sessions.
 *
 * <p>This class holds information about the trainer, including their schedule,
 * assigned sessions, and working hours. It supports the application's business
 * logic for managing trainers and their availability.</p>
 *
 * @author Pavlo Poliak
 * @since 1.0
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "Trainer.trainees.specialization",
        attributeNodes = {
                @NamedAttributeNode("trainees"),
                @NamedAttributeNode("specialization")
        }
)
@Table(name = "trainer")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "training_type_id", nullable = false)
    private TrainingType specialization;

    @Builder.Default
    @OneToMany(mappedBy = "trainer")
    private Set<Training> trainings = new HashSet<>();

    @Builder.Default
    @ManyToMany(mappedBy = "trainers")
    private Set<Trainee> trainees = new HashSet<>();

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TrainingSession> trainingSessions;

    /**
     * Retrieves the username of the current user.
     *
     * <p>This method returns the username from the underlying {@link User} object.
     * It is used to fetch the unique identifier of the user for authentication and
     * authorization processes.</p>
     *
     * @return the username of the current user
     */
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Sets a new password for the user.
     *
     * <p>This method updates the password of the {@code user} entity to the provided {@code newPassword}.</p>
     *
     * @param newPassword the new password to be set. It must meet the application's security requirements.
     */
    public void setPassword(String newPassword) {
        user.setPassword(newPassword);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Trainer trainer = (Trainer) obj;
        return Objects.equals(id, trainer.id)
                && Objects.equals(user, trainer.user)
                && Objects.equals(specialization, trainer.specialization)
                && Objects.equals(trainings, trainer.trainings)
                && Objects.equals(trainees, trainer.trainees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, specialization, trainings, trainees);
    }
}
