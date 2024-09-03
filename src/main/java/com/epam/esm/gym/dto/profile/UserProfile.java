package com.epam.esm.gym.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Represents a user profile containing personal and account information.
 *
 * <p>This class encapsulates details of a user, such as their name, username, and account status.</p>
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Boolean active;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UserProfile profile = (UserProfile) obj;
        return Objects.equals(id, profile.id)
                && Objects.equals(firstName, profile.firstName)
                && Objects.equals(lastName, profile.lastName)
                && Objects.equals(username, profile.username)
                && Objects.equals(password, profile.password)
                && Objects.equals(active, profile.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, username, password, active);
    }
}
