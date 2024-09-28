package com.epam.esm.gym.user.provider.trainee;

import com.epam.esm.gym.user.dto.trainer.TrainerProfile;
import com.epam.esm.gym.user.dto.training.TrainingTypeDto;
import com.epam.esm.gym.user.entity.Specialization;
import com.epam.esm.gym.user.entity.Trainer;
import com.epam.esm.gym.user.entity.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

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
        TrainingTypeDto transfiguration = TrainingTypeDto.builder()
                .specialization(Specialization.TRANSFIGURATION)
                .build();

        TrainingTypeDto defense = TrainingTypeDto.builder()
                .specialization(Specialization.DEFENSE)
                .build();

        List<TrainerProfile> updatedTrainers = List.of(
                TrainerProfile.builder()
                        .username("Minerva.McGonagall")
                        .firstName("Minerva")
                        .lastName("McGonagall")
                        .specialization((transfiguration))
                        .build(),
                TrainerProfile.builder()
                        .username("Remus.Lupin")
                        .firstName("Remus")
                        .lastName("Lupin")
                        .specialization((defense))
                        .build()
        );

        return Stream.of(
                Arguments.of(
                        "Harry.Potter",
                        List.of(
                                Trainer.builder().user(User.builder().username("Minerva.McGonagall").build()).build(),
                                Trainer.builder().user(User.builder().username("Remus.Lupin").build()).build()
                        ), updatedTrainers
                ),
                Arguments.of(
                        "Harry.Potter",
                        List.of(Trainer.builder().user(User.builder().username("Albus.Dumbledore").build()).build()),
                        updatedTrainers
                )
        );
    }
}
