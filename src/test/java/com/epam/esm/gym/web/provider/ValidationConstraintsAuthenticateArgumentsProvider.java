package com.epam.esm.gym.web.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class ValidationConstraintsAuthenticateArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of("", "", "size must be between 2 and 50, size must be between 6 and 50"),
                Arguments.of("Harry.Potter", "short", "size must be between 6 and 50"),
                Arguments.of("", "Password123", "size must be between 2 and 50"),
                Arguments.of("", "short", "size must be between 2 and 50, size must be between 6 and 50"),
                Arguments.of("Harry.Potter", "", "size must be between 6 and 50"),
                Arguments.of("Harry.Potter", "ThisPasswordIsWayTooLongForTheDefinedConstraintViolationException", "size must be between 6 and 50")
        );
    }
}
