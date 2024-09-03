package com.epam.esm.gym.web.provider.trainer;

import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides arguments for testing scenarios involving trainer data.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply test data related to trainers.
 * It is used to verify that the system correctly handles various trainer data scenarios, including
 * the creation, modification, and retrieval of trainer information.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class TrainerArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        User harry = User.builder()
                .id(1)
                .firstName("Harry")
                .lastName("Potter")
                .username("Harry.Potter")
                .password("password123")
                .active(true)
                .permission(RoleType.ROLE_TRAINER)
                .build();

        User hermione = User.builder()
                .id(2)
                .firstName("Hermione")
                .lastName("Granger")
                .username("Hermione.Granger")
                .password("password456")
                .active(true)
                .permission(RoleType.ROLE_TRAINER)
                .build();

        Trainer trainer1 = Trainer.builder().user(harry).build();
        Trainer trainer2 = Trainer.builder().user(hermione).build();
        return Stream.of(
                Arguments.of(List.of(trainer1, trainer2), "ron"),
                Arguments.of(List.of(trainer2), "harry"),
                Arguments.of(List.of(trainer1), "hermione")
        );
    }
}
