package com.epam.esm.gym.web.provider.trainee;

import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

public class PasswordChangeArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {

        ProfileRequest requestNewPasswordSameAsOld = new ProfileRequest("Harry.Potter", "oldPassword", "oldPassword");
        ProfileRequest requestValidChange = new ProfileRequest("Harry.Potter", "oldPassword", "newPassword");

        return Stream.of(
                Arguments.of(requestNewPasswordSameAsOld, new MessageResponse("Old password is incorrect", HttpStatus.BAD_REQUEST)),
                Arguments.of(requestValidChange, new MessageResponse("Password updated successfully", HttpStatus.ACCEPTED))
        );
    }
}
