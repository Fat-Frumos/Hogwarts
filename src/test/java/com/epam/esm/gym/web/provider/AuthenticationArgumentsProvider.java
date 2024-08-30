package com.epam.esm.gym.web.provider;

import com.epam.esm.gym.domain.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

public class AuthenticationArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        User validUser = new User(1, "Harry", "Potter", "Harry.Potter", "correctPassword", true);
        User invalidUser = new User(2, "Hermione", "Granger", "Hermione.Granger", "wrongPassword", true);

        return Stream.of(
                Arguments.of("Hermione.Granger", "incorrectPassword", validUser, HttpStatus.UNAUTHORIZED),
                Arguments.of("Harry.Potter", "wrongPassword", invalidUser, HttpStatus.UNAUTHORIZED),
                Arguments.of("Harry.Potter", "correctPassword", validUser, HttpStatus.OK)
        );
    }
}
