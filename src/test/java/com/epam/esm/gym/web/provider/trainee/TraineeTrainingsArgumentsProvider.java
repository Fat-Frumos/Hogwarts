package com.epam.esm.gym.web.provider.trainee;

import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class TraineeTrainingsArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        List<TrainingResponse> trainingResponses = List.of(
                TrainingResponse.builder()
                        .trainerName("Minerva McGonagall")
                        .trainingName("Advanced Transfiguration")
                        .trainingType("TRANSFIGURATION")
                        .trainingDuration(60)
                        .trainingDate(LocalDate.of(2024, 1, 10))
                        .build(),
                TrainingResponse.builder()
                        .trainerName("Severus Snape")
                        .trainingName("Potions Mastery")
                        .trainingType("TRANSFIGURATION")
                        .trainingDuration(90)
                        .trainingDate(LocalDate.of(2024, 1, 15))
                        .build()
        );

        return Stream.of(
                Arguments.of(
                        "Harry.Potter",
                        TrainingProfile.builder()
                                .periodFrom(LocalDate.of(2024, 1, 1))
                                .periodTo(LocalDate.of(2024, 12, 31))
                                .trainerName("Minerva McGonagall")
                                .trainingType("TRANSFIGURATION")
                                .build(),
                        ResponseEntity.ok(trainingResponses)
                )
        );
    }
}
