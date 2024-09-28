package com.epam.esm.gym.user.provider;

import com.epam.esm.gym.user.dto.trainee.TraineeProfileResponse;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides arguments for parameterized tests involving TraineeProfileResponse.
 * This class implements the ArgumentsProvider interface to supply test data
 * for various test cases that require a list of TraineeProfileResponse.
 */
public class TraineeProfileResponseProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        TraineeProfileResponse potter = TraineeProfileResponse.builder()
                .username("harry.potter")
                .firstName("Harry")
                .lastName("Potter")
                .address("Hogwarts")
                .active(true)
                .build();

        TraineeProfileResponse ron = TraineeProfileResponse.builder()
                .username("ron.weasley")
                .firstName("Ron")
                .lastName("Weasley")
                .address("The Burrow")
                .active(true)
                .build();

        return Stream.of(
                Arguments.of(List.of(potter)),
                Arguments.of(List.of(ron))
        );
    }
}
