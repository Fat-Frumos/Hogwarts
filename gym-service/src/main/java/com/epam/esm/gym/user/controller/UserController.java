package com.epam.esm.gym.user.controller;

import com.epam.esm.gym.user.dto.profile.UserResponse;
import com.epam.esm.gym.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * @return a list of {@link com.epam.esm.gym.user.dto.profile.UserResponse} objects.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(service.findAll());
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
