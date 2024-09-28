package com.epam.esm.gym.user.provider.trainer;

import com.epam.esm.gym.user.dto.trainer.TrainerProfile;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides arguments for the getAllTrainers test case.
 * Supplies a list of TrainerProfile objects for testing the retrieval of all trainers.
 */
public class GetAllTrainersArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        List<TrainerProfile> trainers = List.of(
                TrainerProfile.builder().username("Severus.Snape").firstName("Severus").lastName("Snape").build(),
                TrainerProfile.builder().username("Remus.Lupin").firstName("Remus").lastName("Lupin").build()
        );
        return Stream.of(Arguments.of(trainers));
    }
}
