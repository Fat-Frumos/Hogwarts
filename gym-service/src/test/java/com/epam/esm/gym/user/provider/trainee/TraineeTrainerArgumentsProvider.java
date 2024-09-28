package com.epam.esm.gym.user.provider.trainee;

import com.epam.esm.gym.user.entity.RoleType;
import com.epam.esm.gym.user.entity.Trainee;
import com.epam.esm.gym.user.entity.Trainer;
import com.epam.esm.gym.user.entity.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.stream.Stream;

/**
 * Provides arguments for testing scenarios involving relationships between trainees and trainers.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply input data specifically for
 * test cases that involve interactions or relationships between trainees and trainers. It helps
 * in verifying the behavior of the system when dealing with such relationships.</p>
 *
 * <p>The provided arguments cover various scenarios, including valid and invalid associations
 * between trainees and trainers, ensuring that the system handles these relationships correctly.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class TraineeTrainerArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        User harry = User.builder()
                .id(1)
                .firstName("Harry")
                .lastName("Potter")
                .username("Harry.Potter")
                .password("password123")
                .active(true)
                .permission(RoleType.ROLE_TRAINEE)
                .build();

        Trainee trainee1 = Trainee.builder()
                .id(1L)
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .user(harry)
                .trainings(new HashSet<>())
                .build();

        User hermione = User.builder()
                .id(2)
                .firstName("Hermione")
                .lastName("Granger")
                .username("Hermione.Granger")
                .password("password456")
                .active(true)
                .permission(RoleType.ROLE_TRAINEE)
                .build();

        Trainee trainee2 = Trainee.builder()
                .id(2L)
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .user(hermione)
                .trainings(new HashSet<>())
                .build();


        Trainer trainer1 = Trainer.builder().user(harry).build();
        Trainer trainer2 = Trainer.builder().user(hermione).build();

        return Stream.of(
                Arguments.of(trainee1, trainer1),
                Arguments.of(trainee2, trainer2)
        );
    }
}
