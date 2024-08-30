package com.epam.esm.gym.web.provider.trainer;

import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.stream.Stream;

public class TrainerMissingRegistrationArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of(
                        TrainerRequest.builder()
                                .lastName("Sirius")
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build(),
                        ResponseEntity.badRequest().body(new MessageResponse("FirstName is required", HttpStatus.BAD_REQUEST)),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TrainerRequest.builder()
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build(),
                        ResponseEntity.badRequest().body(new MessageResponse("FirstName is required, LastName is required", HttpStatus.BAD_REQUEST)),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TrainerRequest.builder()
                                .lastName("Sirius")
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .build(),
                        ResponseEntity.badRequest().body(new MessageResponse("FirstName is required", HttpStatus.BAD_REQUEST)),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TrainerRequest.builder().build(),
                        ResponseEntity.badRequest().body(new MessageResponse("FirstName is required, LastName is required", HttpStatus.BAD_REQUEST)),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TrainerRequest.builder().lastName("Potter").build(),
                        ResponseEntity.badRequest().body(new MessageResponse("FirstName is required", HttpStatus.BAD_REQUEST)),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TrainerRequest.builder().firstName("Harry").build(),
                        ResponseEntity.badRequest().body(new MessageResponse("LastName is required", HttpStatus.BAD_REQUEST)),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TrainerRequest.builder()
                                .firstName("Remus")
                                .address("Hogwarts")
                                .build(),
                        ResponseEntity.badRequest().body(new MessageResponse("LastName is required", HttpStatus.BAD_REQUEST)),
                        HttpStatus.BAD_REQUEST
                )
        );
    }
}
