package com.epam.esm.gym.web.provider;

import com.epam.esm.gym.dto.auth.MessageResponse;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

/**
 * Provides arguments for parameterized tests related to authentication.
 *
 * <p>This class implements the {@link ArgumentsProvider} interface to supply test arguments
 * for parameterized tests. It generates a stream of {@link Arguments} representing various
 * scenarios for authentication testing, such as valid and invalid username-password pairs.</p>
 *
 * <p>It is used to create multiple sets of input data for tests, allowing for a comprehensive
 * evaluation of authentication logic under different conditions. The provided arguments are
 * utilized in tests to ensure the correctness and robustness of authentication functionalities.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class AuthenticateArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of("Harry.Potter", "Password123",
                        new ResponseEntity<>(new MessageResponse("Authentication successful"), HttpStatus.OK)),
                Arguments.of("Harry.Potter", "WrongPassword",
                        new ResponseEntity<>(new MessageResponse("Invalid credentials"), HttpStatus.UNAUTHORIZED)),
                Arguments.of("NonExistentUser", "Password123",
                        new ResponseEntity<>(new MessageResponse("User not found"), HttpStatus.NOT_FOUND))
        );
    }
}
