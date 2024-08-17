package com.epam.esm.gym.service;

import com.epam.esm.gym.dto.TrainingTypeResponseDto;
import java.util.List;

public interface TrainingService {
    List<TrainingTypeResponseDto> getTrainingTypes();
}
