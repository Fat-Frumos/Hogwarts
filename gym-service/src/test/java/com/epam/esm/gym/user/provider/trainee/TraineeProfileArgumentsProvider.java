package com.epam.esm.gym.user.provider.trainee;

import com.epam.esm.gym.user.dto.trainee.FullTraineeProfileResponse;
import com.epam.esm.gym.user.entity.Specialization;
import com.epam.esm.gym.user.dto.trainee.PutTraineeRequest;
import com.epam.esm.gym.user.dto.trainer.TrainerResponse;
import com.epam.esm.gym.user.dto.training.TrainingTypeDto;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

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

        TrainingTypeDto poison = TrainingTypeDto.builder()
                .specialization(Specialization.POTIONS)
                .build();

        List<TrainerResponse> trainers = List.of(
                TrainerResponse.builder()
                        .username("Pomona.Sprout")
                        .firstName("Pomona")
                        .lastName("Sprout")
                        .build(),
                TrainerResponse.builder()
                        .username("Severus.Snape")
                        .firstName("Severus")
                        .lastName("Snape")
                        .specialization((poison))
                        .build()
        );

        FullTraineeProfileResponse profile = FullTraineeProfileResponse.builder()
                .username("Harry.Potter")
                .firstName("Harry")
                .lastName("Potter")
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .active(true)
                .trainers(trainers)
                .build();

        PutTraineeRequest request = PutTraineeRequest.builder()
                .username("Harry.Potter")
                .firstName("Harry")
                .lastName("Potter")
                .active(true)
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .build();

        return Stream.of(
                Arguments.of(
                        "Harry.Potter",
                        profile, request
                )
        );
    }
}
