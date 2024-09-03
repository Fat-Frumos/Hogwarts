package com.epam.esm.gym.dao.storage;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import com.epam.esm.gym.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents the data structure for storing and loading data from JSON.
 *
 * <p>This class maps the JSON structure containing lists of various entities such as users, trainers,
 * trainees, and trainings. It uses {@link JsonProperty} annotations to bind JSON property names to the
 * respective fields in the class. The fields are private and use getter and setter methods to access
 * and modify the data. This class is typically used to deserialize JSON data into Java objects.</p>
 */
@Getter
@Setter
public class StorageData {

    @JsonProperty("users")
    private List<User> users;

    @JsonProperty("trainers")
    private List<Trainer> trainers;

    @JsonProperty("trainees")
    private List<Trainee> trainees;

    @JsonProperty("trainings")
    private List<Training> trainings;
}
