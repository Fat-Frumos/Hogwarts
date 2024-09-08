package com.epam.esm.gym.security;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class that implements {@link UserDetailsService}.
 * <p>This service provides user details for authentication purposes.</p>
 */
@Slf4j
@Service
@AllArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    /**
     * Loads user details by username.
     *
     * @param username the username of the user whose details are to be loaded.
     * @return a {@link UserPrincipal} object containing the user's details.
     * @throws UsernameNotFoundException if no user is found with the given username.
     */
    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userDao.findByUsername(username);
        if (userOptional.isEmpty()) {
            log.error("User Not Found");
            throw new UsernameNotFoundException("user not found");
        }
        return new UserPrincipal(userOptional.get());
    }
}
