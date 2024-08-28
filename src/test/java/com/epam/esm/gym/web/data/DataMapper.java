package com.epam.esm.gym.web.data;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.dto.training.TrainingProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DataMapper {

    public static TrainerRequest getTrainerRequest(Map<String, Object> trainers) {
        return TrainerRequest.builder()
                .firstName((String) trainers.get("firstName"))
                .lastName((String) trainers.get("lastName"))
                .specialization(Specialization.valueOf(((String) trainers.get("specialization")).toUpperCase()))
                .build();
    }

    public static TrainingResponse getTrainingResponse(Map<String, Object> training) {
        return TrainingResponse.builder()
                .trainerName((String) training.get("trainerUsername"))
                .trainingName((String) training.get("trainingName"))
                .trainingType((String) training.get("trainingType"))
                .trainingDuration((Integer) training.get("trainingDuration"))
                .trainingDate(LocalDate.parse((String) training.get("trainingDate")))
                .build();
    }

    public static TrainerProfile getTrainerProfile(Map<String, Object> trainer) {
        return TrainerProfile.builder()
                .username((String) trainer.get("username"))
                .firstName((String) trainer.get("firstName"))
                .lastName((String) trainer.get("lastName"))
                .specialization(TrainingType.builder().trainingType((Specialization) trainer.get("specialization")).build())
                .active((Boolean) trainer.get("active"))
                .trainees(getTraineeProfile(List.of(TraineeData.ronMap, TraineeData.harryMap, TraineeData.hermioneMap)))
                .build();
    }

    public static TrainingProfile getTrainingProfile(Map<String, Object> training) {
        return TrainingProfile.builder()
                .periodFrom(LocalDate.parse((String) training.get("trainingDate")))
                .periodTo(LocalDate.now())
                .trainerName((String) training.get("trainerUsername"))
                .trainingType((String) training.get("trainingType"))
                .build();
    }

    public static TraineeProfile getTraineeProfile(Map<String, Object> map) {
        return TraineeProfile.builder()
                .username((String) map.get("username"))
                .firstName((String) map.get("firstName"))
                .lastName((String) map.get("lastName"))
                .dateOfBirth(LocalDate.parse((String) map.get("dateOfBirth")))
                .address((String) map.get("address"))
                .active((Boolean) map.get("active"))
                .build();
    }

    public static List<TraineeProfile> getTraineeProfile(List<Map<String, Object>> traineeMaps) {
        return traineeMaps.stream()
                .map(DataMapper::getTraineeProfile)
                .toList();
    }

    public static List<TrainerProfile> getTrainerProfiles(List<Map<String, Object>> trainerMaps) {
        return trainerMaps.stream()
                .map(DataMapper::getTrainerProfile)
                .toList();
    }
}
