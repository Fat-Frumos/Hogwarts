package com.epam.esm.gym.web.data;

import static com.epam.esm.gym.domain.Specialization.DEFENSE;
import static com.epam.esm.gym.domain.Specialization.POTIONS;
import static com.epam.esm.gym.domain.Specialization.TRANSFIGURATION;
import com.epam.esm.gym.dto.training.TrainingTypeResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainingData {

    public static Map<String, Object> training;
    public static List<TrainingTypeResponse> trainingTypes;

    static {
        trainingTypes = List.of(
                new TrainingTypeResponse(POTIONS, 1L),
                new TrainingTypeResponse(DEFENSE, 2L),
                new TrainingTypeResponse(TRANSFIGURATION, 3L)
        );

        training = new HashMap<>();
        training.put("traineeUsername", "Parry.Potter");
        training.put("trainerUsername", "Severus.Snape");
        training.put("trainingName", "Potions Mastery");
        training.put("trainingType", "Potions");
        training.put("trainingDate", "2024-08-01");
        training.put("trainingDuration", 2);
    }
}
