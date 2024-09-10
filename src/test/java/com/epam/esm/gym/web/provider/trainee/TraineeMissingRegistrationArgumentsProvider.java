package com.epam.esm.gym.web.provider.trainee;

import com.epam.esm.gym.dto.auth.MessageResponse;
import com.epam.esm.gym.dto.trainee.PutTraineeRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * Provides arguments for test cases involving scenarios where a trainee's registration is missing.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply test data for scenarios where
 * a trainee's registration details are either incomplete or missing. This helps in testing how the
 * system handles cases where expected registration information is not available.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class TraineeMissingRegistrationArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(
                        PutTraineeRequest.builder()
                                .lastName("Potter")
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build(),
                        ResponseEntity.badRequest().body(new MessageResponse(
                                "Required request parameter 'firstName' is not present")),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        PutTraineeRequest.builder()
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build(),
                        ResponseEntity.badRequest().body(new MessageResponse(
                                "Required request parameter 'firstName' is not present, " +
                                        "Required request parameter 'lastName' is not present")),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        PutTraineeRequest.builder()
                                .lastName("Potter")
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .build(),
                        ResponseEntity.badRequest().body(new MessageResponse(
                                "Required request parameter 'firstName' is not present")),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        PutTraineeRequest.builder().build(),
                        ResponseEntity.badRequest().body(new MessageResponse(
                                "Required request parameter 'firstName' is not present, " +
                                        "Required request parameter 'lastName' is not present")),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        PutTraineeRequest.builder().lastName("Potter").build(),
                        ResponseEntity.badRequest().body(new MessageResponse(
                                "Required request parameter 'firstName' is not present")),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        PutTraineeRequest.builder().firstName("Harry").build(),
                        ResponseEntity.badRequest().body(new MessageResponse(
                                "Required request parameter 'lastName' is not present")),
                        HttpStatus.BAD_REQUEST
                ),
                Arguments.of(
                        PutTraineeRequest.builder()
                                .firstName("Harry")
                                .address("Hogwarts")
                                .build(),
                        ResponseEntity.badRequest().body(new MessageResponse(
                                "Required request parameter 'lastName' is not present")),
                        HttpStatus.BAD_REQUEST
                ));
    }
}
