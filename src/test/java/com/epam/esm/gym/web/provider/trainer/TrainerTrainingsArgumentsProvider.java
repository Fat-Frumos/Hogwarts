package com.epam.esm.gym.web.provider.trainer;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

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
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {

        TrainingProfile trainingProfile = TrainingProfile.builder()
                .trainingType(Specialization.DEFENSE.name())
                .build();

        List<TrainingResponse> trainingResponses = List.of(
                TrainingResponse.builder().trainingType(Specialization.DEFENSE.name()).build()
        );

        return Stream.of(
                Arguments.of("Remus.Lupin", trainingProfile, ResponseEntity.ok(trainingResponses))
        );
    }
}
