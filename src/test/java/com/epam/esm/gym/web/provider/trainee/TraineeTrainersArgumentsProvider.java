package com.epam.esm.gym.web.provider.trainee;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Stream;

public class TraineeTrainersArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        TrainingType transfiguration = TrainingType.builder()
                .trainingType(Specialization.TRANSFIGURATION)
                .build();

        TrainingType defense = TrainingType.builder()
                .trainingType(Specialization.DEFENSE)
                .build();

        List<TrainerProfile> updatedTrainers = List.of(
                TrainerProfile.builder()
                        .username("Minerva.McGonagall")
                        .firstName("Minerva")
                        .lastName("McGonagall")
                        .specialization(transfiguration)
                        .build(),
                TrainerProfile.builder()
                        .username("Remus.Lupin")
                        .firstName("Remus")
                        .lastName("Lupin")
                        .specialization(defense)
                        .build()
        );

        return Stream.of(
                Arguments.of(
                        "Harry.Potter",
                        List.of("Minerva.McGonagall", "Remus.Lupin"),
                        ResponseEntity.ok(updatedTrainers)
                ),
                Arguments.of(
                        "Harry.Potter",
                        List.of("Albus.Dumbledore"),
                        ResponseEntity.ok(updatedTrainers)
                )
        );
    }
}
