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

/**
 * Provides arguments for testing scenarios related to missing trainer registration.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply various sets of input data
 * for test cases involving scenarios where a trainer's registration is missing or incomplete.
 * These scenarios help to validate the system's handling and responses when dealing with
 * trainers who are not properly registered or whose registration details are not available.</p>
 *
 * <p>The provided arguments simulate different conditions under which a trainer's registration
 * might be missing, enabling comprehensive testing of error handling, validation, and response
 * mechanisms for such cases.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
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
                        ResponseEntity.badRequest().body(new MessageResponse(
                                "Required request parameter 'firstName' is not present")),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TrainerRequest.builder()
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build(),
                        ResponseEntity.badRequest().body(new MessageResponse(
                                "Required request parameter 'specialization' is not present, " +
                                        "Required request parameter 'firstName' is not present, " +
                                        "Required request parameter 'specialization' is not present")),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TrainerRequest.builder()
                                .lastName("Sirius")
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .build(),
                        ResponseEntity.badRequest().body(new MessageResponse(
                                "Required request parameter 'firstName' is not present")),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TrainerRequest.builder().build(),
                        ResponseEntity.badRequest().body(new MessageResponse(
                                "Required request parameter 'firstName' is not present, " +
                                        "Required request parameter 'specialization' is not present, " +
                                        "Required request parameter 'specialization' is not present")),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TrainerRequest.builder().lastName("Potter").build(),
                        ResponseEntity.badRequest().body(new MessageResponse(
                                "Required request parameter 'firstName' is not present")),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TrainerRequest.builder().firstName("Harry").build(),
                        ResponseEntity.badRequest().body(new MessageResponse(
                                "Required request parameter 'specialization' is not present,")),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        TrainerRequest.builder()
                                .firstName("Remus")
                                .address("Hogwarts")
                                .build(),
                        ResponseEntity.badRequest().body(new MessageResponse(
                                "Required request parameter 'specialization' is not present,")),
                        HttpStatus.BAD_REQUEST
                )
        );
    }
}
