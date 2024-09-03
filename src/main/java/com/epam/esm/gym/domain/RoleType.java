package com.epam.esm.gym.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.epam.esm.gym.domain.Permission.ADMIN_CREATE;
import static com.epam.esm.gym.domain.Permission.ADMIN_DELETE;
import static com.epam.esm.gym.domain.Permission.ADMIN_READ;
import static com.epam.esm.gym.domain.Permission.ADMIN_UPDATE;
import static com.epam.esm.gym.domain.Permission.TRAINEE_READ;
import static com.epam.esm.gym.domain.Permission.TRAINER_CREATE;
import static com.epam.esm.gym.domain.Permission.TRAINER_READ;
import static com.epam.esm.gym.domain.Permission.TRAINER_UPDATE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

/**
 * Enumeration for user roles and their associated permissions.
 *
 * <p>This enum represents different roles such as TRAINEE, TRAINER, and ADMIN, each with a specific set of
 * permissions. The roles include permissions that control access to various system functionalities.</p>
 */
@Getter
@AllArgsConstructor
public enum RoleType {

    ROLE_TRAINEE(new HashSet<>(singletonList(TRAINEE_READ))),

    ROLE_TRAINER(new HashSet<>(asList(
            TRAINER_CREATE,
            TRAINER_UPDATE,
            TRAINER_READ))),

    ROLE_ADMIN(new HashSet<>(asList(
            ADMIN_CREATE,
            ADMIN_READ,
            ADMIN_UPDATE,
            ADMIN_DELETE)));

    private final Set<Permission> authorities;

    /**
     * Retrieves a list of granted authorities for the role.
     *
     * <p>This method converts the set of permissions associated with the role into
     * a list of {@link SimpleGrantedAuthority} objects. It also adds the role itself
     * as an authority to the list. This list is used by Spring Security to manage
     * access control based on the user's role and permissions.</p>
     *
     * @return a list of {@link SimpleGrantedAuthority} representing the authorities
     * granted by this role
     */
    public List<SimpleGrantedAuthority> getGrantedAuthorities() {
        List<SimpleGrantedAuthority> grantedAuthorities =
                getAuthorities().stream()
                        .map(permission -> new SimpleGrantedAuthority(
                                permission.getAuthority()))
                        .collect(toList());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthorities;
    }
}