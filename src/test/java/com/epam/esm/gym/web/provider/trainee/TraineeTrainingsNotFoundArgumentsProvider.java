package com.epam.esm.gym.web.provider.trainee;

import com.epam.esm.gym.dto.training.TrainingProfile;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Stream;

import static java.time.LocalDate.parse;

public class TraineeTrainingsNotFoundArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        TrainingProfile profile = TrainingProfile.builder()
                .periodFrom(parse("2023-01-01"))
                .periodTo(parse("2023-12-31"))
                .trainerName("Minerva.McGonagall")
                .trainingType("Transfiguration")
                .build();
        return Stream.of(
                Arguments.of(
                        "Hogwarts",
                        ResponseEntity.ok().body(List.of()),
                        profile
                ),
                Arguments.of(
                        "Transfiguration",
                        ResponseEntity.ok().body(List.of()),
                        profile
                )
        );
    }
}
