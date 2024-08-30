package com.epam.esm.gym.web.provider.trainer;

import com.epam.esm.gym.dto.training.TrainingTypeResponse;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.epam.esm.gym.domain.Specialization.DEFENSE;
import static com.epam.esm.gym.domain.Specialization.POTIONS;
import static com.epam.esm.gym.domain.Specialization.TRANSFIGURATION;

public class TrainingArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        Map<String, Object> training = new HashMap<>();
        training.put("traineeUsername", "Parry.Potter");
        training.put("trainerUsername", "Severus.Snape");
        training.put("trainingName", "Potions Mastery");
        training.put("trainingType", "Potions");
        training.put("trainingDate", "2024-08-01");
        training.put("trainingDuration", 2);

        List<TrainingTypeResponse> trainingTypes = List.of(
                new TrainingTypeResponse(POTIONS, 1L),
                new TrainingTypeResponse(DEFENSE, 2L),
                new TrainingTypeResponse(TRANSFIGURATION, 3L)
        );

        return Stream.of(
                Arguments.of(training, ResponseEntity.status(201).build()),
                Arguments.of(trainingTypes, ResponseEntity.ok(trainingTypes))
        );
    }
}
