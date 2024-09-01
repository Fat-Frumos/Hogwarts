package com.epam.esm.gym.web.provider.trainee;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.stream.Stream;

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
//                .permission(RoleType.TRAINER)
                .build();

        Trainee trainee = Trainee.builder()
                .id(1L)
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .user(harry)
                .trainings(new HashSet<>())
                .build();

        TraineeProfile profile1 = TraineeProfile.builder()
                .firstName("Harry")
                .lastName("Potter")
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .active(true)
                .build();

        ResponseEntity<TraineeProfile> response1 = ResponseEntity
                .status(HttpStatus.OK)
                .body(profile1);

        TraineeProfile profile2 = TraineeProfile.builder()
                .firstName("Hermione")
                .lastName("Granger")
                .dateOfBirth(LocalDate.parse("1979-09-19"))
                .address("Hogwarts")
                .active(true)
                .build();

        ResponseEntity<TraineeProfile> response2 = ResponseEntity
                .status(HttpStatus.OK)
                .body(profile2);

        return Stream.of(
                Arguments.of("Harry.Potter", trainee, response1),
                Arguments.of("Hermione.Granger", trainee, response2)
        );
    }
}
