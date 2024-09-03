package com.epam.esm.gym.web.provider.trainee;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.training.TrainingResponse;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Provides arguments for testing scenarios involving trainees and their associated training sessions.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply input data specifically for
 * test cases that focus on trainee-training relationships. It helps verify the system's
 * behavior when dealing with various training sessions associated with trainees.</p>
 *
 * <p>The provided arguments include different combinations of trainees and their corresponding
 * training sessions, ensuring thorough testing of how the system handles these relationships.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class TraineeTrainingArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        TrainingResponse trainingResponse = new TrainingResponse(
                "Minerva McGonagall",
                "Advanced Transfiguration",
                "TRANSFIGURATION",
                60,
                LocalDate.of(2024, 1, 10)
        );

        Training training = Training.builder()
                .trainingDate(LocalDate.of(2024, 1, 10))
                .trainer(Trainer.builder().user(User.builder().username("Minerva McGonagall").build()).build())
                .type(TrainingType.builder().trainingType(Specialization.TRANSFIGURATION).build())
                .build();

        Trainee trainee = Trainee.builder()
                .user(User.builder().username("Harry.Potter").build())
                .trainings(Set.of(training))
                .build();

        Map<String, String> minerva = Map.of(
                "periodFrom", "2024-01-01",
                "periodTo", "2024-12-31",
                "trainerName", "Minerva McGonagall",
                "trainingType", "TRANSFIGURATION"
        );

        Map<String, String> period = Map.of(
                "periodFrom", "2024-01-01",
                "periodTo", "2024-12-31"
        );

        Map<String, String> gonagall = Map.of(
                "trainerName", "Minerva McGonagall"
        );

        return Stream.of(
                Arguments.of(minerva, trainee, training, trainingResponse),
                Arguments.of(period, trainee, training, trainingResponse),
                Arguments.of(gonagall, trainee, training, trainingResponse)
        );
    }
}
