package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.PutTrainerRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    void testToDto_withValidTrainer() {
        // Arrange
        User trainerUser = User.builder()
                .username("trainerUsername")
                .firstName("TrainerFirstName")
                .lastName("TrainerLastName")
                .build();

        Trainer trainer = Trainer.builder()
                .user(trainerUser)
                .trainingTypes(Arrays.asList(
                        TrainingType.builder().specialization(Specialization.CARDIO).build(),
                        TrainingType.builder().specialization(Specialization.STRENGTH).build()))
                .trainees(new HashSet<>(Collections.singletonList(
                        Trainee.builder()
                                .user(User.builder().firstName("TraineeFirstName")
                                        .lastName("TraineeLastName")
                                        .username("traineeUsername")
                                        .active(true).build())
                                .address("TraineeAddress")
                                .dateOfBirth(LocalDate.parse("2000-01-01"))
                                .build())))
                .build();

        BaseResponse dto = mapper.toDto(trainer);
        assertNotNull(dto);
    }

    @Test
    void testToDto_withNullTrainer() {
        BaseResponse dto = mapper.toDto(null);

        assertNull(dto);
    }

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
        Trainer granger = Trainer.builder()
                .user(User.builder()
                        .firstName("Hermione")
                        .lastName("Granger")
                        .username("Hermione.Granger")
                        .active(true)
                        .build())
                .build();
        PutTrainerRequest request = PutTrainerRequest.builder()
                .firstName("Hermione")
                .lastName("Granger")
                .active(true)
                .build();

        Trainer trainer = mapper.toEntity(request, granger);

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
