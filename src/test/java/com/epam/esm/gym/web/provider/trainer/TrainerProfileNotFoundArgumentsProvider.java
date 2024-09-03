package com.epam.esm.gym.web.provider.trainer;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

/**
 * Provides arguments for testing scenarios where trainer profiles are not found.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply various sets of input data
 * for test cases that involve scenarios where a trainer profile is not found. These scenarios
 * help validate the system's behavior when attempting to access or operate on trainer profiles
 * that do not exist or are unavailable.</p>
 *
 * <p>The provided arguments simulate different conditions under which a trainer profile might
 * be missing, enabling comprehensive testing of error handling and response mechanisms for
 * such cases.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class TrainerProfileNotFoundArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        TrainerRequest request = TrainerRequest.builder()
                .firstName("Alice")
                .lastName("Bob")
                .specialization(Specialization.DEFENSE.name())
                .build();

        MessageResponse response = MessageResponse.builder()
                .message("Trainer not found").build();

        return Stream.of(
                Arguments.of(request, ResponseEntity.status(HttpStatus.NOT_FOUND).body(response))
        );
    }
}
