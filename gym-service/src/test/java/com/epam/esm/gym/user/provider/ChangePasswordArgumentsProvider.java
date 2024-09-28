package com.epam.esm.gym.user.provider;

import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.profile.ProfileRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

/**
 * Provides arguments for testing password change scenarios.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply various sets of input data
 * for test cases related to changing a user's password. It covers different scenarios to
 * validate the system's response to valid and invalid password change requests.</p>
 *
 * <p>The provided arguments include both valid and invalid password change requests to
 * ensure comprehensive testing of the password change functionality, including edge cases and
 * error handling.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class ChangePasswordArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        ProfileRequest correctPasswordRequest = new ProfileRequest(
                "Harry.Potter", "Password123", "newPassword123");
        ProfileRequest incorrectOldPasswordRequest = new ProfileRequest(
                "Harry.Potter", "WrongPassword", "newPassword123");
        ProfileRequest sameAsOldPasswordRequest = new ProfileRequest(
                "Harry.Potter", "Password123", "Password123");
        ProfileRequest userNotFoundRequest = new ProfileRequest(
                "NonExistentUser", "Password123", "newPassword123");

        return Stream.of(
                Arguments.of(correctPasswordRequest, new ResponseEntity<>(
                        new MessageResponse("Password updated successfully"), HttpStatus.ACCEPTED)),
                Arguments.of(incorrectOldPasswordRequest, new ResponseEntity<>(
                        new MessageResponse("Old password is incorrect"), HttpStatus.PAYMENT_REQUIRED)),
                Arguments.of(sameAsOldPasswordRequest, new ResponseEntity<>(
                        new MessageResponse("New password is the same as the old password"), HttpStatus.BAD_REQUEST)),
                Arguments.of(userNotFoundRequest, new ResponseEntity<>(
                        new MessageResponse("User not found"), HttpStatus.NOT_FOUND))
        );
    }
}
