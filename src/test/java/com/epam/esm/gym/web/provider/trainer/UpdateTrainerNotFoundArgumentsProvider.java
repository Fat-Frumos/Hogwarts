package com.epam.esm.gym.web.provider.trainer;

import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Provides arguments for testing scenarios where a trainer to be updated is not found.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply various inputs to test cases
 * where an attempt to update a trainer results in a "not found" situation. The provided
 * arguments are used to simulate different scenarios and validate the controller's behavior
 * when the trainer does not exist in the system.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class UpdateTrainerNotFoundArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        TrainerUpdateRequest updateRequest = TrainerUpdateRequest.builder()
                .firstName("NonExistent")
                .lastName("Trainer")
                .build();

        return Stream.of(
                Arguments.of("NonExistentTrainer", updateRequest, ResponseEntity.status(BAD_REQUEST).build())
        );
    }
}
