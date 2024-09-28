package com.epam.esm.gym.user.provider.trainee;

import com.epam.esm.gym.user.dto.trainee.PutTraineeRequest;
import com.epam.esm.gym.user.entity.Trainee;
import com.epam.esm.gym.user.entity.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDate;
import java.util.stream.Stream;


/**
 * Provides arguments for testing scenarios involving Update Trainee.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply input data specifically for
 * test cases that focus on responses related to trainees .
 * It ensures that the system correctly processes and returns responses for various
 * trainee-training interactions.</p>
 *
 * <p>The provided arguments include various combinations of trainee responses,
 * facilitating comprehensive testing of how the system handles these responses.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class UpdateTraineeArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        User baseUser = User.builder()
                .username("Harry.Potter")
                .firstName("Harry")
                .lastName("Potter")
                .active(true)
                .build();

        Trainee initialTrainee = Trainee.builder()
                .user(baseUser)
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .build();

        return Stream.of(
                Arguments.of(
                        PutTraineeRequest.builder()
                                .username("Harry.Potter")
                                .firstName("Harry")
                                .lastName("Potter")
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .active(true)
                                .build(),
                        initialTrainee,
                        Trainee.builder()
                                .user(baseUser)
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build()
                ),
                Arguments.of(
                        PutTraineeRequest.builder()
                                .username("Harry.Potter")
                                .firstName("Harry")
                                .lastName("Potter")
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .active(true)
                                .build(),
                        initialTrainee,
                        Trainee.builder()
                                .user(baseUser)
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build()
                ),
                Arguments.of(
                        PutTraineeRequest.builder()
                                .username("Harry.Potter")
                                .firstName("Harry")
                                .lastName("Potter")
                                .active(true)
                                .build(),
                        initialTrainee,
                        Trainee.builder()
                                .user(User.builder()
                                        .username("Harry.Potter")
                                        .firstName("Harry")
                                        .lastName("Potter")
                                        .active(true)
                                        .build())
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build()
                ),
                Arguments.of(
                        PutTraineeRequest.builder()
                                .username("Harry.Potter")
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .active(true)
                                .build(),
                        initialTrainee,
                        Trainee.builder()
                                .user(User.builder()
                                        .username("Harry.Potter")
                                        .firstName("Harry")
                                        .lastName("Potter")
                                        .active(true)
                                        .build())
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build()
                ),
                Arguments.of(
                        PutTraineeRequest.builder()
                                .username("Harry.Potter")
                                .firstName("Harry")
                                .lastName("Potter")
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build(),
                        initialTrainee,
                        Trainee.builder()
                                .user(baseUser)
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build()
                ),
                Arguments.of(
                        PutTraineeRequest.builder()
                                .firstName("Harry")
                                .lastName("Potter")
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .active(true)
                                .build(),
                        initialTrainee,
                        Trainee.builder()
                                .user(User.builder()
                                        .username("Harry.Potter")
                                        .firstName("Harry")
                                        .lastName("Potter")
                                        .active(true)
                                        .build())
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build()
                ),
                Arguments.of(
                        PutTraineeRequest.builder()
                                .active(true)
                                .build(),
                        initialTrainee,
                        Trainee.builder()
                                .user(baseUser)
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build()
                ),
                Arguments.of(
                        PutTraineeRequest.builder()
                                .address("Hogwarts")
                                .active(true)
                                .build(),
                        initialTrainee,
                        Trainee.builder()
                                .user(baseUser)
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build()
                ),
                Arguments.of(
                        PutTraineeRequest.builder()
                                .username("Harry.Potter")
                                .firstName("Harry")
                                .lastName("Potter")
                                .build(),
                        initialTrainee,
                        Trainee.builder()
                                .user(baseUser)
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build()
                ),
                Arguments.of(
                        PutTraineeRequest.builder()
                                .lastName("Potter")
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .active(true)
                                .build(),
                        initialTrainee,
                        Trainee.builder()
                                .user(baseUser)
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build()
                ),
                Arguments.of(
                        PutTraineeRequest.builder()
                                .username("Harry.Potter")
                                .build(),
                        initialTrainee,
                        Trainee.builder()
                                .user(User.builder()
                                        .username("Harry.Potter")
                                        .firstName("Harry")
                                        .lastName("Potter")
                                        .active(true)
                                        .build())
                                .dateOfBirth(LocalDate.parse("1980-07-31"))
                                .address("Hogwarts")
                                .build()
                )
        );
    }
}
