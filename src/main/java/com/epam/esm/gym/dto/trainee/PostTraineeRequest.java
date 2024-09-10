package com.epam.esm.gym.dto.trainee;

import com.epam.esm.gym.dto.auth.BaseResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;

/**
 * Represents a request for creating a trainee.
 * Implements the {@link BaseResponse} interface.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PostTraineeRequest implements BaseResponse {

    @NotBlank(message = "Required request parameter 'firstName' is not present")
    @Size(min = 1, max = 50, message = "Firstname must be between 1 and 50 characters")
    private String firstName;

    @NotBlank(message = "Required request parameter 'lastName' is not present")
    @Size(min = 1, max = 50, message = "LastName must be between 1 and 50 characters")
    private String lastName;

    private LocalDate dateOfBirth;
    private String address;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        PostTraineeRequest that = (PostTraineeRequest) obj;

        return new EqualsBuilder()
                .append(firstName, that.firstName)
                .append(lastName, that.lastName)
                .append(dateOfBirth, that.dateOfBirth)
                .append(address, that.address).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(firstName)
                .append(lastName)
                .append(dateOfBirth)
                .append(address).toHashCode();
    }
}
