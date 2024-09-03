package com.epam.esm.gym.web.provider.trainer;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

/**
 * Provides arguments for testing scenarios where trainer registrations exist.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply test data for scenarios where
 * trainer registrations are present. It is used to verify that the system correctly
 * processes and responds when there are existing registrations for trainers.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class ExistsTrainerRegistrationsArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        TrainerRequest request = TrainerRequest.builder()
                .firstName("Remus")
                .lastName("Lupin")
                .specialization(Specialization.TRANSFIGURATION.name())
                .build();

        ProfileResponse response = ProfileResponse.builder()
                .username("Remus.Lupin")
                .password("Password123")
                .build();

        return Stream.of(
                Arguments.of(
                        "Remus.Lupin",
                        ResponseEntity.ok(response),
                        request
                )
        );
    }
}
