package com.epam.esm.gym.dto.trainer;

import com.epam.esm.gym.dto.auth.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a request to create or update a trainer, containing details required for such operations.
 *
 * <p>This class is used to encapsulate the data necessary to create or update a trainer.</p>
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SlimTrainerResponse implements BaseResponse {
    private String username;
    private String firstName;
    private String lastName;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        SlimTrainerResponse that = (SlimTrainerResponse) obj;

        return new EqualsBuilder()
                .append(username, that.username)
                .append(firstName, that.firstName)
                .append(lastName, that.lastName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(username).append(firstName)
                .append(lastName).toHashCode();
    }
}
