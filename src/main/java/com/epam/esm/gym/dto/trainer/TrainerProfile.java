package com.epam.esm.gym.dto.trainer;

import com.epam.esm.gym.dto.trainee.SlimTraineeProfileResponse;
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
public class TrainerProfile extends TrainerResponse {
    private List<SlimTraineeProfileResponse> trainees;

    /**
     * Constructs a new {@code TrainerProfile} with the specified username, first name, last name, and list of trainees.
     *
     * @param username the username of the trainer
     * @param first    the first name of the trainer
     * @param last     the last name of the trainer
     * @param trainees a list of {@link com.epam.esm.gym.dto.trainee.SlimTraineeProfileResponse}
     *                 representing the trainees associated with the trainer
     */
    public TrainerProfile(String username, String first, String last, List<SlimTraineeProfileResponse> trainees) {
        super(username, first, last);
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
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(trainees, profile.trainees)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(trainees).toHashCode();
    }
}
