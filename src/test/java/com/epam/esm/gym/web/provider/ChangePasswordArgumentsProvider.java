package com.epam.esm.gym.web.provider;

import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

public class ChangePasswordArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        ProfileRequest correctPasswordRequest = new ProfileRequest("Harry.Potter", "Password123", "newPassword123");
        ProfileRequest incorrectOldPasswordRequest = new ProfileRequest("Harry.Potter", "WrongPassword", "newPassword123");
        ProfileRequest sameAsOldPasswordRequest = new ProfileRequest("Harry.Potter", "Password123", "Password123");
        ProfileRequest userNotFoundRequest = new ProfileRequest("NonExistentUser", "Password123", "newPassword123");

        return Stream.of(
                Arguments.of(correctPasswordRequest, new ResponseEntity<>(new MessageResponse("Password updated successfully", HttpStatus.ACCEPTED), HttpStatus.ACCEPTED)),
                Arguments.of(incorrectOldPasswordRequest, new ResponseEntity<>(new MessageResponse("Old password is incorrect", HttpStatus.PAYMENT_REQUIRED), HttpStatus.PAYMENT_REQUIRED)),
                Arguments.of(sameAsOldPasswordRequest, new ResponseEntity<>(new MessageResponse("New password is the same as the old password", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST)),
                Arguments.of(userNotFoundRequest, new ResponseEntity<>(new MessageResponse("User not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND))
        );
    }
}
