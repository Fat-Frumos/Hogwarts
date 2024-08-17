package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TraineeDao;
import com.epam.esm.gym.dto.ResponseRegistrationDto;
import com.epam.esm.gym.dto.TraineeRequestRegistrationDto;
import org.springframework.stereotype.Service;

@Service
public class DefaultTraineeService implements TraineeService {

    private final TraineeDao dao;

    public DefaultTraineeService(TraineeDao dao) {
        this.dao = dao;
    }

    @Override
    public ResponseRegistrationDto registerTrainee(
            TraineeRequestRegistrationDto dto) {
        return null;
    }
}
