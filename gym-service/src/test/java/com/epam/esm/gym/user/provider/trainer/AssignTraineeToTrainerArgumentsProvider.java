package com.epam.esm.gym.user.provider.trainer;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Provides arguments for the assignTraineeToTrainer test case.
 * Supplies a trainee's username and a success message for testing the trainee assignment to a trainer.
 */
public class AssignTraineeToTrainerArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        String traineeUsername = "Harry.Potter";
        String expectedMessage = "Trainee assigned successfully";

        return Stream.of(Arguments.of(traineeUsername, expectedMessage));
    }
}