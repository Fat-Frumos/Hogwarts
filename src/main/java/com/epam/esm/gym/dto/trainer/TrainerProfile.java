package com.epam.esm.gym.dto.trainer;

import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

/**
 * Data Transfer Object for representing a trainer profile.
 *
 * <p>This DTO includes the trainer's username, first and last name,
 * specialization, active status, and associated trainees.</p>
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfile extends SlimTrainerProfile {
    private List<TraineeProfile> trainees;

    /**
     * Constructs a new {@link TrainerProfile} with the specified details.
     *
     * @param username       the username of the trainer.
     * @param firstName      the first name of the trainer.
     * @param lastName       the last name of the trainer.
     * @param specialization the specialization of the trainer.
     * @param active         the status indicating whether the trainer is active.
     * @param trainees       the list of trainees associated with the trainer.
     */
    public TrainerProfile(
            String username, String firstName,
            String lastName, TrainingType specialization,
            boolean active, List<TraineeProfile> trainees) {
        super(username, firstName, lastName, specialization, active);
        this.trainees = trainees;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        TrainerProfile profile = (TrainerProfile) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj))
                .append(trainees, profile.trainees).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(trainees).toHashCode();
    }
}
