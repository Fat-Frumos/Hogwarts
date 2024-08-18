package com.epam.esm.gym.web.data;

import com.epam.esm.gym.dto.trainee.TraineeRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TraineeData {

    public static Map<String, Object> activateTrainee;
    public static Map<String, String> registration;
    public static Map<String, Object> trainees;
    public static TraineeRequest traineeRequest;

    static {

        registration = new HashMap<>();
        registration.put("firstName", "Harry");
        registration.put("lastName", "Potter");
        registration.put("dateOfBirth", "1980-07-31");
        registration.put("address", "4 Privet Drive, Little Whinging, Surrey");

        trainees = new HashMap<>();
        trainees.put("username", "harry.potter");
        trainees.put("firstName", "Harry");
        trainees.put("lastName", "Potter");
        trainees.put("dateOfBirth", "1980-07-31");
        trainees.put("address", "12 Grimmauld Place, London");
        trainees.put("isActive", true);

        activateTrainee = new HashMap<>();
        activateTrainee.put("username", "harry.potter");
        activateTrainee.put("isActive", false);

        traineeRequest = new TraineeRequest();
        traineeRequest.setFirstName("Harry");
        traineeRequest.setLastName("Potter");
        traineeRequest.setDateOfBirth(LocalDate.of(1980, 7, 31));
        traineeRequest.setAddress("4 Privet Drive, Little Whinging, Surrey");
    }
}
