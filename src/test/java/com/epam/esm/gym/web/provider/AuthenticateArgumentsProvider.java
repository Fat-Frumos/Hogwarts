package com.epam.esm.gym.web.provider;

import com.epam.esm.gym.dto.profile.MessageResponse;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

public class AuthenticateArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of("Harry.Potter", "Password123", new ResponseEntity<>(new MessageResponse("Authentication successful", HttpStatus.OK.value()), HttpStatus.OK)),
                Arguments.of("Harry.Potter", "WrongPassword", new ResponseEntity<>(new MessageResponse("Invalid credentials", HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED)),
                Arguments.of("NonExistentUser", "Password123", new ResponseEntity<>(new MessageResponse("User not found", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND))
        );
    }
}

