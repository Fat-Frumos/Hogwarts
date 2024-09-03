package com.epam.esm.gym.web.provider.trainee;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

/**
 * Provides arguments for testing scenarios where trainee profiles are not found.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply input data specifically
 * for test cases where trainee profiles are expected to be absent or not found. It helps
 * ensure that the system correctly handles cases where requested profiles do not exist.</p>
 *
 * <p>The provided arguments include various cases of non-existent profiles to validate the
 * system's response to such situations, ensuring robustness in error handling and user feedback.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class NotFoundTraineeProfileArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of("Bob", ResponseEntity.notFound().build()),
                Arguments.of("Alice", ResponseEntity.notFound().build())
        );
    }
}
