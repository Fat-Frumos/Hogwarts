package com.epam.esm.gym.web.provider.trainee;

import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.stream.Stream;

public class TraineeRegistrationArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(
                        TraineeRequest.builder()
                                .firstName("Harry")
                                .lastName("Potter")
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build(),
                        ResponseEntity.ok(getProfileResponse()),
                        HttpStatus.CREATED),
                Arguments.of(
                        TraineeRequest.builder()
                                .firstName("Harry")
                                .lastName("Potter")
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .build(),
                        ResponseEntity.ok(getProfileResponse()),
                        HttpStatus.CREATED),
                Arguments.of(
                        TraineeRequest.builder()
                                .firstName("Harry")
                                .lastName("Potter")
                                .address("Hogwarts")
                                .build(),
                        ResponseEntity.ok(getProfileResponse()),
                        HttpStatus.CREATED),
                Arguments.of(
                        TraineeRequest.builder()
                                .firstName("Harry")
                                .lastName("Potter")
                                .build(),
                        ResponseEntity.ok(getProfileResponse()),
                        HttpStatus.CREATED)
        );
    }

    private static ProfileResponse getProfileResponse() {
        return ProfileResponse.builder()
                .username("harry.potter")
                .password("generated_password")
                .build();
    }
}
