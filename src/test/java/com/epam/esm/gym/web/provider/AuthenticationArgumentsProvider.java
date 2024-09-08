package com.epam.esm.gym.web.provider;

import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

/**
 * Provides arguments for parameterized tests related to authentication.
 *
 * <p>This class implements the {@link ArgumentsProvider} interface to supply test arguments
 * for parameterized tests. It generates a stream of {@link Arguments} representing various
 * authentication scenarios, such as different username-password pairs and edge cases.</p>
 *
 * <p>Using this provider, you can create tests that cover multiple authentication scenarios,
 * including valid and invalid credentials, ensuring comprehensive coverage of authentication
 * logic in your application.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class AuthenticationArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {

        User validUser = User.builder()
                .id(1)
                .firstName("Harry")
                .lastName("Potter")
                .username("Harry.Potter")
                .password("Password123")
                .active(true)
                .permission(RoleType.ROLE_TRAINER)
                .build();

        User invalidUser = User.builder()
                .id(2)
                .firstName("Hermione")
                .lastName("Granger")
                .username("Hermione.Granger")
                .password("wrongPassword")
                .active(true)
                .permission(RoleType.ROLE_TRAINEE)
                .build();

        return Stream.of(
                Arguments.of("Hermione.Granger", "incorrectPassword", validUser, HttpStatus.UNAUTHORIZED),
                Arguments.of("Harry.Potter", "wrongPassword", invalidUser, HttpStatus.UNAUTHORIZED),
                Arguments.of("Harry.Potter", "Password123", validUser, HttpStatus.UNAUTHORIZED)
        );
    }
}
