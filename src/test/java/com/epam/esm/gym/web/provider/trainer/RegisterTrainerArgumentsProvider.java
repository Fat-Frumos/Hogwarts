package com.epam.esm.gym.web.provider.trainer;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.stream.Stream;

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
                Arguments.of(request, ResponseEntity.ok(response))
        );
    }
}
