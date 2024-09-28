package com.epam.esm.gym.user.provider.trainer;

import com.epam.esm.gym.user.dto.trainer.TrainerProfile;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides arguments for the getNotAssignedActiveTrainers test case.
 * Supplies a trainer's username and the expected list of active, unassigned TrainerProfile for testing retrieval.
 */
public class GetNotAssignedActiveTrainersArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        String username = "Dolores.Umbridge";
        List<TrainerProfile> trainers = List.of(
                TrainerProfile.builder().username("Gilderoy.Lockhart")
                        .firstName("Gilderoy").lastName("Lockhart").build(),
                TrainerProfile.builder().username("Quirinus.Quirrell")
                        .firstName("Quirinus").lastName("Quirrell").build()
        );

        return Stream.of(Arguments.of(username, trainers));
    }
}
