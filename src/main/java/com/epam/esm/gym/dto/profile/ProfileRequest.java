package com.epam.esm.gym.dto.profile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * Represents a request to update user profile details, including username and passwords.
 *
 * <p>This class is used for requests that involve updating a user's profile, ensuring that
 * all required fields are present and validated.</p>
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {

    @NotNull(message = "Username is required")
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters")
    private String username;

    @NotNull(message = "Password is required")
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    private String oldPassword;

    @NotNull(message = "New password is required")
    @Size(min = 6, max = 50, message = "New password must be between 6 and 50 characters")
    private String newPassword;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ProfileRequest that = (ProfileRequest) obj;
        return Objects.equals(username, that.username)
                && Objects.equals(oldPassword, that.oldPassword)
                && Objects.equals(newPassword, that.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, oldPassword, newPassword);
    }
}
