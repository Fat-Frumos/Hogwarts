package com.epam.esm.gym.user.provider.trainer;

import com.epam.esm.gym.user.dto.profile.ProfileResponse;
import com.epam.esm.gym.user.dto.trainer.TrainerRequest;
import com.epam.esm.gym.user.entity.Specialization;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * Provides arguments for testing scenarios related to registering trainers.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply test data for scenarios where
 * trainers are being registered. It is used to verify that the system correctly processes
 * registration requests for trainers and handles various edge cases or validation scenarios.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class RegisterTrainerArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        TrainerRequest request = TrainerRequest.builder()
                .firstName("Remus")
                .lastName("Lupin")
                .dateOfBirth(LocalDate.parse("1960-03-10"))
                .address("Hogwarts")
                .specialization(Specialization.DEFENSE.name())
                .build();

        ProfileResponse response = ProfileResponse.builder()
                .username("Remus.Lupin")
                .password("password")
                .build();

        return Stream.of(
                Arguments.of(request, response)
        );
    }
}
