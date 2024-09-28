package com.epam.esm.gym.user.provider.trainer;

import com.epam.esm.gym.jms.dto.MessageResponse;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Provides arguments for the updateStatusTrainer test case.
 * Supplies a trainer's username, active status, and expected MessageResponse for testing status updates.
 */
public class UpdateStatusTrainerArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        String username = "Horace.Slughorn";
        Boolean active = true;
        MessageResponse response = new MessageResponse("Trainer Horace Slughorn activated successfully");

        return Stream.of(Arguments.of(username, active, response));
    }
}