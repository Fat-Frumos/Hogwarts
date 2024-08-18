package com.epam.esm.gym.web.data;

import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import java.util.HashMap;
import java.util.Map;

public class TrainerData {
    public static final Map<String, Object> activateTrainer = new HashMap<>();
    public static final Map<String, String> registration = new HashMap<>();
    public static final Map<String, Object> horaceMap = new HashMap<>();
    public static final Map<String, Object> snapeMap = new HashMap<>();
    public static final TrainerUpdateRequest UPDATE_REQUEST;
    public static final TrainerProfile TRAINER_PROFILE;

    static {

        registration.put("firstName", "Severus");
        registration.put("lastName", "Snape");
        registration.put("specialization", "Potions");

        snapeMap.put("username", "Severus.Snape");
        snapeMap.put("firstName", "Severus");
        snapeMap.put("lastName", "Snape");
        snapeMap.put("specialization", "Potions");
        snapeMap.put("active", true);

        horaceMap.put("username", "Horace.Slughorn");
        horaceMap.put("firstName", "Horace");
        horaceMap.put("lastName", "Slughorn");
        horaceMap.put("specialization", "Potions");
        horaceMap.put("active", true);

        activateTrainer.put("specialization", "Potions");
        activateTrainer.put("username", "Severus.Snape");
        activateTrainer.put("firstName", "Severus");
        activateTrainer.put("lastName", "Snape");
        activateTrainer.put("active", false);

        TRAINER_PROFILE = TrainerProfile.builder()
                .username("Severus.Snape")
                .firstName("Severus")
                .lastName("Snape")
                .specialization("Potions")
                .active(true)
                .build();

        UPDATE_REQUEST = TrainerUpdateRequest.builder()
                .firstName("Horace")
                .lastName("Slughorn")
                .active(false)
                .build();
    }
}
