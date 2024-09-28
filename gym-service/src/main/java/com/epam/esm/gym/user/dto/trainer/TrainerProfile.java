package com.epam.esm.gym.user.dto.trainer;

import com.epam.esm.gym.jms.dto.TrainerRequest;
import com.epam.esm.gym.jms.dto.TrainerStatus;
import com.epam.esm.gym.user.dto.trainee.TraineeProfileResponse;
import com.epam.esm.gym.user.dto.training.TrainingTypeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
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
public class TrainerProfile extends TrainerRequest {
    private TrainingTypeDto specialization;
    private List<TraineeProfileResponse> trainees;

    /**
     * Constructs a new {@code TrainerProfile} with the specified username,
     * first name, last name, and list of trainees.
     *
     * @param username  the username of the trainer
     * @param firstName the first name of the trainer
     * @param lastName  the last name of the trainer
     * @param trainees  a list of {@link TraineeProfileResponse}
     *                  representing the trainees associated with the trainer
     */
    public TrainerProfile(
            String username, String firstName, String lastName,
            List<TraineeProfileResponse> trainees) {
        super(username, firstName, lastName, TrainerStatus.ACTIVE, new ArrayList<>());
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

        TrainerProfile that = (TrainerProfile) obj;

        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(specialization, that.specialization)
                .append(trainees, that.trainees)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(specialization)
                .append(trainees)
                .toHashCode();
    }
}
