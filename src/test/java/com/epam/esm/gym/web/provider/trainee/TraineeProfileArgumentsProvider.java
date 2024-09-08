package com.epam.esm.gym.web.provider.trainee;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.SlimTrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

/**
 * Provides arguments for testing trainee profile scenarios.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply various sets of input data
 * for test cases related to trainee profiles. It covers different scenarios to ensure
 * proper validation and functionality for operations involving trainee profiles.</p>
 *
 * <p>The provided arguments include a range of valid and invalid profile details, as well
 * as various edge cases, to thoroughly test the system's handling of profile-related operations.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class TraineeProfileArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        TrainingType herbology = TrainingType.builder()
                .specialization(Specialization.HERBOLOGY)
                .build();

        TrainingType posions = TrainingType.builder()
                .specialization(Specialization.POISON)
                .build();

        List<SlimTrainerProfile> trainers = List.of(
                TrainerProfile.builder()
                        .username("Pomona.Sprout")
                        .firstName("Pomona")
                        .lastName("Sprout")
                        .specialization(herbology)
                        .build(),
                TrainerProfile.builder()
                        .username("Severus.Snape")
                        .firstName("Severus")
                        .lastName("Snape")
                        .specialization(posions)
                        .build()
        );

        TraineeProfile profile = TraineeProfile.builder()
                .firstName("Harry")
                .lastName("Potter")
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .active(true)
                .trainers(trainers)
                .build();

        TraineeRequest request = TraineeRequest.builder()
                .firstName("Harry")
                .lastName("Potter")
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .build();

        return Stream.of(
                Arguments.of(
                        "Harry.Potter",
                        ResponseEntity.ok(profile),
                        request
                )
        );
    }
}
