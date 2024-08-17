package com.epam.esm.gym.service;

import com.epam.esm.gym.dto.TrainingTypeResponseDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DefaultTrainingService implements TrainingService {
    @Override
    public List<TrainingTypeResponseDto> getTrainingTypes() {
        return null;
    }
}
