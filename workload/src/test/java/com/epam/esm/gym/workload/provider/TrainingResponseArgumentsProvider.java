package com.epam.esm.gym.workload.provider;

import com.epam.esm.gym.jms.dto.TrainerProfile;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
/**
 * Provides a stream of arguments for parameterized tests involving TrainingResponse instances.
 * This class implements the ArgumentsProvider interface to supply different sets of trainer profiles.
 */
public class TrainingResponseArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        final String trainerName = "Remus.Lupin";

        TrainerProfile profile = TrainerProfile.builder()
                .username(trainerName)
                .firstName("Remus")
                .lastName("Lupin")
                .active(true)
                .trainings(List.of(
                        TrainingResponse.builder()
                                .trainerName(trainerName)
                                .trainingDate(LocalDate.of(2023, 1, 1))
                                .trainingDuration(60)
                                .build()
                ))
                .build();

        List<TrainingResponse> trainingResponses = List.of(
                TrainingResponse.builder()
                        .trainerName(trainerName)
                        .trainingDate(LocalDate.of(2023, 1, 1))
                        .trainingDuration(60)
                        .build()
        );

        return Stream.of(
                Arguments.of(trainingResponses, profile, trainerName)
        );
    }
}
