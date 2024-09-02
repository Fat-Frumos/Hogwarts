package com.epam.esm.gym.web.provider;

import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

public class ChangePasswordValidationConstraintsArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of(new ProfileRequest("", "", ""), new ResponseEntity<>(new MessageResponse("Username must be between 1 and 50 characters, Password must be between 6 and 50 characters, Password must be between 6 and 50 characters", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST)),
                Arguments.of(new ProfileRequest(null, "Password123", "newPassword123"), new ResponseEntity<>(new MessageResponse("Username is required", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST)),
                Arguments.of(new ProfileRequest("Harry.Potter", "", "newPassword123"), new ResponseEntity<>(new MessageResponse("Password must be between 6 and 50 characters", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST)),
                Arguments.of(new ProfileRequest("Harry.Potter", "Password123", ""), new ResponseEntity<>(new MessageResponse("New password must be between 6 and 50 characters", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST)),
                Arguments.of(new ProfileRequest("Harry.Potter", "Password123", "short"), new ResponseEntity<>(new MessageResponse("New password must be between 6 and 50 characters", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST)),
                Arguments.of(new ProfileRequest("Harry.Potter", "Password123", "ThisPasswordIsWayTooLongForTheDefinedConstraint"), new ResponseEntity<>(new MessageResponse("New password must be between 6 and 50 characters", HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST))
        );
    }
}
