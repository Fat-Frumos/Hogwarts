package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
//@ExtendWith(MockitoExtension.class)
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
    private final String password = "Password123!";

    protected Map<String, String> generateUserCredentials(String firstName, String lastName) {
        String username = firstName.toLowerCase() + "." + lastName.toLowerCase();
        return Map.of("username", username, "password", password);
    }

    protected ProfileResponse getProfileResponse(Map<String, String> registration) {
        String username = registration.get("firstName") + "." + registration.get("lastName");
        return new ProfileResponse(username, password);
    }

    protected TrainerRequest getTrainerRequest(Map<String, Object> trainers) {
        return TrainerRequest.builder()
                .firstName((String) trainers.get("firstName"))
                .lastName((String) trainers.get("lastName"))
                .specialization((String) trainers.get("specialization"))
                .build();
    }
}
