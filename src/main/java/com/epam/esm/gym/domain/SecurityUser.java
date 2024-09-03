package com.epam.esm.gym.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

/**
 * Implementation of {@link UserDetails} representing a security user.
 *
 * <p>This class wraps a {@link User} entity and provides access to user details required for authentication.
 * It includes methods for retrieving authorities, username, and password.</p>
 */
@Getter
@Builder
@ToString
public class SecurityUser implements UserDetails {

    private User user;

    /**
     * Retrieves a list of granted authorities for the role.
     *
     * <p>This method converts the set of permissions associated with the role into
     * a list of {@link org.springframework.security.core.authority.SimpleGrantedAuthority} objects.
     * It also adds the role it self as an authority to the list. This list is used by Spring Security to manage
     * access control based on the user's role and permissions.</p>
     *
     * @return a list of {@link org.springframework.security.core.authority.SimpleGrantedAuthority}
     * representing the authorities granted by this role
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Hibernate.initialize(user.getPermission());
        return user.getPermission().getGrantedAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SecurityUser that = (SecurityUser) obj;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
