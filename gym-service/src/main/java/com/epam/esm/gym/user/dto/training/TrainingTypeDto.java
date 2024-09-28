package com.epam.esm.gym.user.dto.training;

import com.epam.esm.gym.user.entity.Specialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the response containing details about a training type.
 *
 * <p>This class provides the necessary details regarding a type of training, including the specialization it represents
 * and its unique identifier.</p>
 */
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTypeDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -123L;
    private Specialization specialization;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TrainingTypeDto that = (TrainingTypeDto) obj;
        return specialization == that.specialization;
    }

    @Override
    public int hashCode() {
        return Objects.hash(specialization);
    }
}
