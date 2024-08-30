package com.epam.esm.gym.web.provider.trainee;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class TraineeProfileArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        TrainingType herbology = TrainingType.builder()
                .trainingType(Specialization.HERBOLOGY)
                .build();

        TrainingType potions = TrainingType.builder()
                .trainingType(Specialization.POTIONS)
                .build();

        List<TrainerProfile> trainers = List.of(
                TrainerProfile.builder()
                        .username("Pomona.Sprout")
                        .firstName("Pomona")
                        .lastName("Sprout")
                        .specialization(herbology)
                        .build(),
                TrainerProfile.builder()
                        .username("Severus.Snape")
                        .firstName("Severus")
                        .lastName("Snape")
                        .specialization(potions)
                        .build()
        );

        TraineeProfile profile = TraineeProfile.builder()
                .firstName("Harry")
                .lastName("Potter")
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .active(true)
                .trainers(trainers)
                .build();

        TraineeRequest request = TraineeRequest.builder()
                .firstName("Harry")
                .lastName("Potter")
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .build();

        return Stream.of(
                Arguments.of(
                        "Harry.Potter",
                        ResponseEntity.ok(profile),
                        request
                )
        );
    }
}
