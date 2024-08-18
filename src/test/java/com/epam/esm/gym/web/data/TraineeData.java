package com.epam.esm.gym.web.data;

import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TraineeData {

    public static Map<String, Object> activateTrainee;
    public static Map<String, String> registration;
    public static final Map<String, Object> ronMap = new HashMap<>();
    public static final Map<String, Object> harryMap = new HashMap<>();
    public static final Map<String, Object> hermioneMap = new HashMap<>();
    public static TraineeRequest traineeRequest;

    static {

        registration = new HashMap<>();
        registration.put("firstName", "Harry");
        registration.put("lastName", "Potter");
        registration.put("dateOfBirth", "1980-07-31");
        registration.put("address", "4 Privet Drive, Little Whinging, Surrey");

        ronMap.put("username", "Ron.Weasley");
        ronMap.put("firstName", "Ron");
        ronMap.put("lastName", "Weasley");
        ronMap.put("dateOfBirth", "1980-03-01");
        ronMap.put("address", "The Burrow, Ottery St Catchpole");
        ronMap.put("active", true);

        harryMap.put("username", "Harry.Potter");
        harryMap.put("firstName", "Harry");
        harryMap.put("lastName", "Potter");
        harryMap.put("dateOfBirth", "1980-07-31");
        harryMap.put("address", "12 Grimmauld Place, London");
        harryMap.put("active", true);

        hermioneMap.put("username", "Hermione.Granger");
        hermioneMap.put("firstName", "Hermione");
        hermioneMap.put("lastName", "Granger");
        hermioneMap.put("dateOfBirth", "1979-09-19");
        hermioneMap.put("address", "Hogwarts School of Witchcraft and Wizardry");
        hermioneMap.put("active", true);

        activateTrainee = new HashMap<>();
        activateTrainee.put("username", "Harry.Potter");
        activateTrainee.put("active", false);

        traineeRequest = new TraineeRequest();
        traineeRequest.setFirstName("Harry");
        traineeRequest.setLastName("Potter");
        traineeRequest.setDateOfBirth(LocalDate.of(1980, 7, 31));
        traineeRequest.setAddress("4 Privet Drive, Little Whinging, Surrey");

    }
}
