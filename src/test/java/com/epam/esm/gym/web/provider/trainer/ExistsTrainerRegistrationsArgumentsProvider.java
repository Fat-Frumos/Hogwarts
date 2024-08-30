package com.epam.esm.gym.web.provider.trainer;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

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
