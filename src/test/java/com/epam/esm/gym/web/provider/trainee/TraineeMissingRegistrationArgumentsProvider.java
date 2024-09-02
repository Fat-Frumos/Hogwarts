package com.epam.esm.gym.web.provider.trainee;

import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.stream.Stream;

public class TraineeMissingRegistrationArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(
                        TraineeRequest.builder()
                                .lastName("Potter")
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build(),
                        ResponseEntity.badRequest().body(new MessageResponse("FirstName is required", HttpStatus.BAD_REQUEST.value())),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TraineeRequest.builder()
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build(),
                        ResponseEntity.badRequest().body(new MessageResponse("FirstName is required, LastName is required", HttpStatus.BAD_REQUEST.value())),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TraineeRequest.builder()
                                .lastName("Potter")
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .build(),
                        ResponseEntity.badRequest().body(new MessageResponse("FirstName is required", HttpStatus.BAD_REQUEST.value())),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TraineeRequest.builder().build(),
                        ResponseEntity.badRequest().body(new MessageResponse("FirstName is required, LastName is required", HttpStatus.BAD_REQUEST.value())),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TraineeRequest.builder().lastName("Potter").build(),
                        ResponseEntity.badRequest().body(new MessageResponse("FirstName is required", HttpStatus.BAD_REQUEST.value())),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TraineeRequest.builder().firstName("Harry").build(),
                        ResponseEntity.badRequest().body(new MessageResponse("LastName is required", HttpStatus.BAD_REQUEST.value())),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TraineeRequest.builder()
                                .firstName("Harry")
                                .address("Hogwarts")
                                .build(),
                        ResponseEntity.badRequest().body(new MessageResponse("LastName is required", HttpStatus.BAD_REQUEST.value())),
                        HttpStatus.BAD_REQUEST
                ));
    }
}
