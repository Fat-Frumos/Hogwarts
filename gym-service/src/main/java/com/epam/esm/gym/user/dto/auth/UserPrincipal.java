package com.epam.esm.gym.user.dto.auth;

import com.epam.esm.gym.user.entity.User;
import lombok.Builder;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

/**
 * Implementation of {@link org.springframework.security.core.userdetails.UserDetails} representing a security user.
 * <p>This class implements {@link org.springframework.security.core.userdetails.UserDetails}
 * and provides the necessary user information for authentication and authorization processes.</p>
 * <p>This class wraps a {@link com.epam.esm.gym.user.entity.User} entity and provides access for authentication.
 * It includes methods for retrieving authorities, username, and password.</p>
 *
 * @param user Constructs a new {@link UserPrincipal} with the given {@link com.epam.esm.gym.user.entity.User} entity.
 */
@Builder
public record UserPrincipal(User user) implements UserDetails {

    /**
     * Constructs a new {@link UserPrincipal} with the given {@link com.epam.esm.gym.user.entity.User} entity.
     *
     * @param user the {@link com.epam.esm.gym.user.entity.User} entity to be used for authentication.
     */
    public UserPrincipal {
    }

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
    public String toString() {
        return "UserPrincipal{" +
                "user=" + user.toString() +
                '}';
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
        UserPrincipal that = (UserPrincipal) obj;
        return Objects.equals(user, that.user);
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.getActive();
    }
}
