package com.epam.esm.gym.web.provider.trainee;

import com.epam.esm.gym.dto.training.TrainingProfile;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Stream;

public class TraineeTrainingsNotFoundArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of(
                        "Hogwarts",
                        ResponseEntity.ok().body(List.of()),
                        new TrainingProfile()
                ),
                Arguments.of(
                        "Transfiguration",
                        ResponseEntity.ok().body(List.of()),
                        new TrainingProfile()
                )
        );
    }
}
