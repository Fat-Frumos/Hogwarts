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

/**
 * Provides arguments for testing scenarios involving response entities of trainee profiles
 * and their associated trainers.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply input data specifically for
 * test cases that involve the response entities of trainee profiles and their associated
 * trainers. It helps ensure that the system correctly handles and returns data for such cases.</p>
 *
 * <p>The provided arguments include various combinations of trainee profiles and their
 * associated trainers, facilitating comprehensive testing of the response handling for these
 * profiles and relationships.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class TraineeTrainersResponseEntityProfileArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        TrainingType transfiguration = TrainingType.builder()
                .specialization(Specialization.TRANSFIGURATION)
                .build();

        TrainingType defense = TrainingType.builder()
                .specialization(Specialization.DEFENSE)
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
