package com.epam.esm.gym.web;

import com.epam.esm.gym.dao.UserDao;
import com.epam.esm.gym.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing users.
 * <p>This controller provides endpoints for retrieving all users, obtaining CSRF tokens, and adding new users.</p>
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    public final UserDao dao;

    /**
     * Retrieves a list of all users.
     *
     * @return a list of {@link User} objects.
     */
    @GetMapping("/users")
    public List<User> getUsers() {
        return dao.findAll();
    }

    /**
     * Retrieves the CSRF token for the current request.
     *
     * @param request the {@link HttpServletRequest} object containing the CSRF token attribute.
     * @return the {@link CsrfToken} object.
     */
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    /**
     * Adds a new user.
     *
     * @param user the {@link User} object to be added.
     * @return the added {@link User} object.
     */
    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        return dao.save(user);
    }
}
