package com.epam.esm.gym.dto.trainer;

import com.epam.esm.gym.dto.profile.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;

/**
 * Represents a request to create or update a trainer, containing details required for such operations.
 *
 * <p>This class is used to encapsulate the data necessary to create or update a trainer.</p>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerResponse {
    private Long id;
    private UserProfile user;
    private Set<String> specializations;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TrainerResponse that = (TrainerResponse) obj;
        return Objects.equals(id, that.id)
                && Objects.equals(user, that.user)
                && Objects.equals(specializations, that.specializations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, specializations);
    }
}
