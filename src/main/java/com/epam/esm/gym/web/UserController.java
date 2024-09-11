package com.epam.esm.gym.web;

import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.UserProfile;
import com.epam.esm.gym.mapper.UserMapper;
import com.epam.esm.gym.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing users.
 * <p>This controller provides endpoints for retrieving all users, and adding new users.</p>
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    public final UserService service;

    /**
     * Retrieves a list of all users.
     *
     * @return a list of {@link UserProfile} objects.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserProfile>> getUsers() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * Adds a new user.
     *
     * @param user the {@link User} object to be added.
     * @return the added {@link UserProfile} object.
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserProfile> addUser(@RequestBody User user) {
        return ResponseEntity.ok().body(UserMapper.toDto(service.saveUser(user)));
    }

    /**
     * Delete a user.
     *
     * @param username the string to be removed.
     * @return the void {@link ResponseEntity} object.
     */
    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String username) {
        return service.deleteUser(username);
    }
}
