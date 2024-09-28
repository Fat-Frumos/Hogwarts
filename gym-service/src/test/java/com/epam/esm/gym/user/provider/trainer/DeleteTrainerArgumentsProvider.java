package com.epam.esm.gym.user.provider.trainer;

import com.epam.esm.gym.jms.dto.MessageResponse;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Provides arguments for the deleteTrainer test case.
 * Supplies a trainer's username and the expected MessageResponse for testing trainer deletion.
 */
public class DeleteTrainerArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        String username = "Minerva.McGonagall";
        MessageResponse response = new MessageResponse("Trainer Minerva McGonagall deleted successfully");
        return Stream.of(Arguments.of(username, response));
    }
}