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
