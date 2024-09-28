package com.epam.esm.gym.user.provider.trainer;

import com.epam.esm.gym.user.dto.trainer.TrainerProfile;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Provides arguments for the getTrainerProfile test case.
 * Supplies a trainer's username and expected TrainerProfile for testing profile retrieval.
 */
public class GetTrainerProfileArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        String username = "Remus.Lupin";
        TrainerProfile profile = TrainerProfile.builder()
                .username(username)
                .firstName("Remus")
                .lastName("Lupin")
                .build();

        return Stream.of(Arguments.of(username, profile));
    }
}