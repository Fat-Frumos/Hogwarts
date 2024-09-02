package com.epam.esm.gym.dao.storage;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.Training;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StorageData {

    private List<Trainer> trainers;
    private List<Trainee> trainees;
    private List<Training> trainings;
}
