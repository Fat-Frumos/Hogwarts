package com.epam.esm.gym.user.provider;

import com.epam.esm.gym.jms.dto.ActionType;
import com.epam.esm.gym.jms.dto.TrainerStatus;
import com.epam.esm.gym.jms.dto.WorkloadRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * Provides arguments for InvalidWorkloadRequest
 *
 * <p>This class implements {@link ArgumentsProvider} to supply various sets of input data
 * for test cases related to changing a user's password. It covers different scenarios to
 * validate the system's response to valid and invalid password change requests.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class InvalidWorkloadRequestArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of(new WorkloadRequest(
                        null,
                        "Harry",
                        "Potter",
                        TrainerStatus.ACTIVE,
                        LocalDate.now().plusDays(1),
                        60,
                        ActionType.ADD
                )),
                Arguments.of(new WorkloadRequest(
                        "Harry.Potter",
                        null,
                        "Potter",
                        TrainerStatus.ACTIVE,
                        LocalDate.now().plusDays(1),
                        60,
                        ActionType.ADD
                )),
                Arguments.of(new WorkloadRequest(
                        "Harry.Potter",
                        "Harry",
                        null,
                        TrainerStatus.ACTIVE,
                        LocalDate.now().plusDays(1),
                        60,
                        ActionType.ADD
                )),
                Arguments.of(new WorkloadRequest(
                        "Harry.Potter",
                        "Harry",
                        "Potter",
                        null,
                        LocalDate.now().plusDays(1),
                        60,
                        ActionType.ADD
                )),
                Arguments.of(new WorkloadRequest(
                        "Harry.Potter",
                        "Harry",
                        "Potter",
                        TrainerStatus.ACTIVE,
                        null,
                        60,
                        ActionType.ADD
                )),
                Arguments.of(new WorkloadRequest(
                        "Harry.Potter",
                        "Harry",
                        "Potter",
                        TrainerStatus.ACTIVE,
                        LocalDate.now().minusDays(1),
                        60,
                        ActionType.ADD
                )),
                Arguments.of(new WorkloadRequest(
                        "Harry.Potter",
                        "Harry",
                        "Potter",
                        TrainerStatus.ACTIVE,
                        LocalDate.now().plusDays(1),
                        60,
                        null
                ))
        );
    }
}
