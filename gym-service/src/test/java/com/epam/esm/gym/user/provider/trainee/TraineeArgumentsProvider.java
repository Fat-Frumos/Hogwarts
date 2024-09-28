package com.epam.esm.gym.user.provider.trainee;

import com.epam.esm.gym.user.dto.trainee.FullTraineeProfileResponse;
import com.epam.esm.gym.user.entity.RoleType;
import com.epam.esm.gym.user.entity.Trainee;
import com.epam.esm.gym.user.entity.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.stream.Stream;

/**
 * Provides arguments for testing scenarios involving trainee data.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply test data related to trainees.
 * It is used to verify that the system correctly processes various trainee scenarios, including
 * the creation, modification, and retrieval of trainee information.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class TraineeArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        User harry = User.builder()
                .id(1)
                .firstName("Harry")
                .lastName("Potter")
                .username("Harry.Potter")
                .password("password123")
                .active(true)
                .permission(RoleType.ROLE_TRAINEE)
                .build();

        Trainee trainee = Trainee.builder()
                .id(1L)
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .user(harry)
                .trainings(new HashSet<>())
                .build();

        FullTraineeProfileResponse profile1 = FullTraineeProfileResponse.builder()
                .firstName("Harry")
                .lastName("Potter")
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .active(true)
                .build();


        FullTraineeProfileResponse profile2 = FullTraineeProfileResponse.builder()
                .firstName("Hermione")
                .lastName("Granger")
                .dateOfBirth(LocalDate.parse("1979-09-19"))
                .address("Hogwarts")
                .active(true)
                .build();


        return Stream.of(
                Arguments.of("Harry.Potter", trainee, profile1),
                Arguments.of("Hermione.Granger", trainee, profile2)
        );
    }
}
