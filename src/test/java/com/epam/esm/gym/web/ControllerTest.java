package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.service.TraineeService;
import com.epam.esm.gym.service.TrainerService;
import com.epam.esm.gym.service.TrainingService;
import com.epam.esm.gym.web.data.DataMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected TrainingService trainingService;
    @MockBean
    protected TrainerService trainerService;
    @MockBean
    protected TraineeService traineeService;
    protected final String password = "Password123!";

    protected ProfileResponse getProfileResponse(String firstName, String lastName) {
        return new ProfileResponse(getUsername(firstName, lastName), password);
    }

    private String getUsername(String firstName, String lastName) {
        return firstName + "." + lastName;
    }

    protected ProfileResponse getProfileResponse(InvocationOnMock invocation) {
        TrainerRequest request = invocation.getArgument(0);
        return getProfileResponse(request.getFirstName(), request.getLastName() + ".1");
    }

    protected ProfileResponse getProfileResponse(Map<String, String> registration) {
        return getProfileResponse(registration.get("firstName"), registration.get("lastName"));
    }

    protected TrainerRequest getTrainerRequest(Map<String, Object> trainer) {
        return DataMapper.getTrainerRequest(trainer);
    }

    protected TrainingResponse getTrainingResponse(Map<String, Object> training) {
        return DataMapper.getTrainingResponse(training);
    }

    protected TrainerProfile getTrainerProfile(Map<String, Object> trainer) {
        return DataMapper.getTrainerProfile(trainer);
    }

    protected TrainingProfile getProfile(Map<String, Object> training) {
        return DataMapper.getTrainingProfile(training);
    }

    protected List<TraineeProfile> getTraineesProfile(List<Map<String, Object>> traineeMaps) {
        return DataMapper.getTraineeProfile(traineeMaps);
    }

    protected List<TrainerProfile> getTrainerProfiles(List<Map<String, Object>> trainers) {
        return DataMapper.getTrainerProfiles(trainers);
    }
}
