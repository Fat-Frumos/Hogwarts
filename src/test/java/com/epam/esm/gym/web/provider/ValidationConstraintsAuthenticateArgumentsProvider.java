package com.epam.esm.gym.web.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Provides arguments for parameterized tests related to validation constraints in authentication.
 *
 * <p>This class implements the {@link ArgumentsProvider} interface to supply test arguments
 * specifically focused on validation constraints for authentication scenarios. It generates
 * a stream of {@link Arguments} representing various username and password values, including
 * edge cases and boundary conditions that test validation logic.</p>
 *
 * <p>Using this provider, you can create tests that ensure your authentication logic properly
 * enforces validation constraints, such as length requirements and invalid characters in
 * usernames and passwords.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class ValidationConstraintsAuthenticateArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of("", "", "size must be between 2 and 50, size must be between 6 and 50"),
                Arguments.of("Harry.Potter", "short", "size must be between 6 and 50"),
                Arguments.of("", "Password123", "size must be between 2 and 50"),
                Arguments.of("", "short", "size must be between 2 and 50, size must be between 6 and 50"),
                Arguments.of("Harry.Potter", "", "size must be between 6 and 50"),
                Arguments.of("Harry.Potter", "a".repeat(51), "size must be between 6 and 50")
        );
    }
}
