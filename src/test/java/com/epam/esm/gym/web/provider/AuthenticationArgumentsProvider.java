package com.epam.esm.gym.web.provider;

import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

public class AuthenticationArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {

        User validUser = User.builder()
                .id(1)
                .firstName("Harry")
                .lastName("Potter")
                .username("Harry.Potter")
                .password("password123")
                .active(true)
                .permission(RoleType.TRAINER)
                .build();

        User invalidUser = User.builder()
                .id(2)
                .firstName("Hermione")
                .lastName("Granger")
                .username("Hermione.Granger")
                .password("wrongPassword")
                .active(true)
                .permission(RoleType.TRAINEE)
                .build();

        return Stream.of(
                Arguments.of("Hermione.Granger", "incorrectPassword", validUser, HttpStatus.UNAUTHORIZED),
                Arguments.of("Harry.Potter", "wrongPassword", invalidUser, HttpStatus.UNAUTHORIZED),
                Arguments.of("Harry.Potter", "correctPassword", validUser, HttpStatus.OK)
        );
    }
}
