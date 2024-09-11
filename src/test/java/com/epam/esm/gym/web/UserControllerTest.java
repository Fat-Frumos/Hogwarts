package com.epam.esm.gym.web;

import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.UserProfile;
import com.epam.esm.gym.service.UserService;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTest {

    @Mock
    private UserService service;


    @WithMockUser(roles = "ADMIN")
    void testGetUsers() throws Exception {
        UserProfile userProfile = UserProfile.builder().username("testUser").build();
        when(service.findAll()).thenReturn(List.of(userProfile));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value("Sybill.Trelawney"));
    }

//    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddUser() throws Exception {
        User user = User.builder()
                .firstName("firstName")
                .password("password")
                .lastName("lastName")
                .active(true)
                .username("firstName.lastName")
                .build();

        when(service.saveUser(user)).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("firstName.lastName"));
    }



    @WithMockUser(roles = "ADMIN")
    void testDeleteUser() throws Exception {
        String username = "firstName.lastName";
        when(service.deleteUser(username)).thenReturn(ResponseEntity.ok().build());
        mockMvc.perform(delete("/api/users/" + username))
                .andExpect(status().isNoContent());
    }

}
