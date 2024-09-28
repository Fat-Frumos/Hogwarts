package com.epam.esm.gym.user.provider.trainer;

import com.epam.esm.gym.user.dto.training.TrainingProfile;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.user.entity.Specialization;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides arguments for testing scenarios related to fetching or assigning trainings for trainers.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply various sets of input data
 * for test cases involving trainers and their associated trainings. The provided arguments
 * help simulate different scenarios related to retrieving or assigning training sessions to
 * trainers, ensuring that the related functionality is thoroughly tested.</p>
 *
 * <p>Each set of arguments can represent different trainers, training sessions, and their
 * relationships. This helps to validate the correctness and robustness of features related
 * to managing trainings for trainers, including proper handling of various edge cases.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class TrainerTrainingsArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        final String trainerName = "Remus.Lupin";
        final String traineeName = "Harry.Potter";
        TrainingProfile trainingProfile = TrainingProfile.builder()
                .traineeName(trainerName)
                .traineeName(traineeName)
                .trainingType(Specialization.DEFENSE.name())
                .build();

        List<TrainingResponse> trainingResponses = List.of(
                TrainingResponse.builder()
                        .trainerName(trainerName)
                        .trainingType(Specialization.DEFENSE.name())
                        .build()
        );

        return Stream.of(
                Arguments.of(trainerName, trainingProfile, trainingResponses)
        );
    }
}
