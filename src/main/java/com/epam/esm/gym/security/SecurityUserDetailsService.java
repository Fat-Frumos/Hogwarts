package com.epam.esm.gym.security;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link UserDetailsService} for loading user-specific data.
 *
 * <p>This service is responsible for fetching user details from the repository and
 * transforming them into a format required by Spring Security. It includes user
 * authentication details such as username, password, and authorities.</p>
 */
@Service
@AllArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserDao userRepository;

    /**
     * Loads user details from the repository by username.
     *
     * <p>This method retrieves a {@link User} object from the repository based on the
     * provided username. If the user is not found, it throws a {@link UsernameNotFoundException}.
     * The retrieved user details are then converted into an {@link org.springframework.security.core.userdetails.User}
     * object, including authorities derived from the user's permissions.</p>
     *
     * @param username the username of the user to be loaded
     * @return a {@link UserDetails} object containing user information for authentication
     * @throws UsernameNotFoundException if the user is not found in the repository
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getPermission().getGrantedAuthorities())
                .accountLocked(!user.getActive())
                .build();
    }
}
