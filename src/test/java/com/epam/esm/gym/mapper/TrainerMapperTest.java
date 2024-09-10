package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link TrainerMapper} class.
 * This class contains tests for the various mapping methods provided by
 * the {@link TrainerMapper} interface. Each test verifies the correctness
 * of converting between different DTOs and entities used in the application.
 */
public class TrainerMapperTest {

    private final TrainerMapper mapper = Mappers.getMapper(TrainerMapper.class);

    @Test
    void toDto() {
        Trainer trainer = Trainer.builder()
                .user(User.builder()
                        .firstName("Harry")
                        .lastName("Potter")
                        .username("harry.potter")
                        .active(true)
                        .build())
                .trainingTypes(List.of(TrainingType.builder().specialization(Specialization.DEFAULT).build()))
                .build();
        BaseResponse profile = mapper.toDto(trainer);
        assertNotNull(profile);
    }

    @Test
    void toEntity() {
        TrainerUpdateRequest request = TrainerUpdateRequest.builder()
                .firstName("Hermione")
                .lastName("Granger")
                .active(true)
                .build();

        Trainer trainer = mapper.toEntity(request);

        assertNotNull(trainer);
        assertEquals("Hermione", trainer.getUser().getFirstName());
        assertEquals("Granger", trainer.getUser().getLastName());
        assertTrue(trainer.getUser().getActive());
    }

    @Test
    void toProfileDto() {
        String username = "ron.weasley";
        String password = "secretPassword";

        ProfileResponse response = mapper.toProfileDto(username, password);

        assertNotNull(response);
        assertEquals(username, response.getUsername());
        assertEquals(password, response.getPassword());
    }

    @Test
    void toTrainerProfiles() {
        Trainer trainer = Trainer.builder()
                .user(User.builder()
                        .username("draco.malfoy")
                        .firstName("Draco")
                        .lastName("Malfoy")
                        .active(true)
                        .build())
                .trainingTypes(List.of(TrainingType.builder().specialization(Specialization.DEFAULT).build()))
                .build();

        List<Trainer> trainers = Collections.singletonList(trainer);

        List<TrainerProfile> profiles = mapper.toTrainerProfiles(trainers);

        assertNotNull(profiles);
        assertEquals(1, profiles.size());
        TrainerProfile profile = profiles.get(0);
        assertEquals("draco.malfoy", profile.getUsername());
        assertEquals("Draco", profile.getFirstName());
        assertEquals("Malfoy", profile.getLastName());
    }

    @Test
    void toTrainer() {
        User user = User.builder()
                .username("albus.dumbledore")
                .firstName("Albus")
                .lastName("Dumbledore")
                .build();
        TrainingType trainingType = TrainingType.builder()
                .specialization(Specialization.DEFAULT)
                .build();
        Trainer trainer = mapper.toTrainer(user, List.of(trainingType));
        assertNotNull(trainer);
        assertEquals(user, trainer.getUser());
        assertEquals(List.of(trainingType), trainer.getTrainingTypes());
    }
}
