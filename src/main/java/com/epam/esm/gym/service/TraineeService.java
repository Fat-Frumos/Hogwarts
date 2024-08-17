package com.epam.esm.gym.service;


import com.epam.esm.gym.dto.ResponseRegistrationDto;
import com.epam.esm.gym.dto.TraineeRequestRegistrationDto;

public interface TraineeService {
    ResponseRegistrationDto registerTrainee(TraineeRequestRegistrationDto request);
}
