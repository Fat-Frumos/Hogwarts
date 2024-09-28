package com.epam.esm.gym.user.controller;

import com.epam.esm.gym.user.dto.profile.UserResponse;
import com.epam.esm.gym.user.entity.RoleType;
import com.epam.esm.gym.user.entity.User;
import com.epam.esm.gym.user.security.service.BruteForceProtectionService;
import com.epam.esm.gym.user.security.service.JwtProvider;
import com.epam.esm.gym.user.security.service.SecurityUserDetailsService;
import com.epam.esm.gym.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
@Import(SecurityConfigTestMock.class)
class UserControllerTest {
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService service;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private BruteForceProtectionService bruteForceProtectionService;
    @MockBean
    private SecurityUserDetailsService userDetailsService;
    private static final String base_url = "/api/users";
    private static final String username = "harry.potter";

    private static final User user;

    static {
        user = new User(1, "harry", "potter",
                username, "invisibility", true, RoleType.ROLE_ADMIN, new HashSet<>());
    }

    private static final UserResponse userResponse = new UserResponse(
            "harry", "potter", username, "invisibility", true);

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUsersReturnsListOfUsers() throws Exception {
        when(service.findAll()).thenReturn(List.of(userResponse));
        mockMvc.perform(get(base_url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value(username));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUserValidUsernameReturnsNoContent() throws Exception {
        when(service.deleteUser(username)).thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        mockMvc.perform(delete(base_url + "/" + username))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUserNonExistentUsernameReturnsNotFound() throws Exception {
        String username = "non_existent";
        String message = "User not found by name non_existent";

        when(service.deleteUser(username)).thenThrow(new EntityNotFoundException(message));
        mockMvc.perform(delete(base_url + "/" + username))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"message\":\"User not found by name non_existent\"}"));
    }

    @Test
    void getUsersAuthorizedReturnsOk() throws Exception {
        when(service.findAll()).thenReturn(Collections.singletonList(userResponse));
        mockMvc.perform(get(base_url)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(
                        Collections.singletonList(userResponse))));
    }

    @Test
    void getUsersUnauthorizedReturnsForbidden() throws Exception {
        mockMvc.perform(get(base_url))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUserUnauthorizedReturnsForbidden() throws Exception {
        mockMvc.perform(delete(base_url + "/{username}", user.getUsername()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUsers() throws Exception {
        UserResponse userProfile = UserResponse.builder().firstName("testUser").build();
        when(service.findAll()).thenReturn(List.of(userProfile));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("testUser"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUser() throws Exception {
        String username = "firstName.lastName";
        when(service.deleteUser(username)).thenReturn(ResponseEntity.noContent().build());
        mockMvc.perform(delete("/api/users/" + username))
                .andExpect(status().isNoContent());
    }
}
