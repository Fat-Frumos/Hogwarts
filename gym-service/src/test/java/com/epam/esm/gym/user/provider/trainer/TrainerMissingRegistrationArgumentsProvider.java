package com.epam.esm.gym.user.provider.trainer;

import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.trainer.TrainerRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

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
                                .lastName("Sirius")
                                .specialization("Specialization")
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build(),
                        (new MessageResponse("Required request parameter 'firstName' is not present"))
                ),
                Arguments.of(TrainerRequest.builder()
                                .lastName("Sirius")
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .build(),
                        new MessageResponse("Required request parameter 'firstName' is not present")
                ),
                Arguments.of(TrainerRequest.builder().lastName("Potter").build(),
                        new MessageResponse("Required request parameter 'firstName' is not present"))
        );
    }
}
