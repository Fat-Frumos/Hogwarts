package com.epam.esm.gym.dto.trainee;

import com.epam.esm.gym.dto.trainer.TrainerResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object for representing a trainer profile.
 *
 * <p>This DTO includes the trainer's username, first and last name,
 * specialization, active status, and associated trainees.</p>
 */
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeProfileResponseResponse extends SlimTraineeProfileResponse {

    private List<TrainerResponse> trainers;

    /**
     * Constructs a new {@link TraineeProfileResponseResponse} with the specified details.
     *
     * @param firstName   the first name of the trainee.
     * @param lastName    the last name of the trainee.
     * @param username    the username of the trainee.
     * @param address     the address of the trainee.
     * @param active      the status indicating whether the trainee is active.
     * @param dateOfBirth the date of birth of the trainee.
     * @param trainers    the list of trainers associated with the trainee.
     */
    public TraineeProfileResponseResponse(
            String firstName, String lastName,
            String username, String address, Boolean active,
            LocalDate dateOfBirth, List<TrainerResponse> trainers) {
        super(firstName, lastName, username, address, active, dateOfBirth);
        this.trainers = trainers;
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
        TraineeProfileResponseResponse profile = (TraineeProfileResponseResponse) obj;
        return new EqualsBuilder()
                .append(trainers, profile.trainers)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(super.hashCode())
                .append(trainers).toHashCode();
    }
}
