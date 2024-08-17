package com.epam.esm.gym.service;

import com.epam.esm.gym.dto.ResponseRegistrationDto;
import com.epam.esm.gym.dto.TrainerDto;
import com.epam.esm.gym.dto.TrainerRegistrationDto;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

public interface TrainerService {

    Optional<TrainerDto> getTrainerByUsername(String username);

    void changeTrainerPassword(String username, String newPassword);

    void updateTrainer(TrainerDto trainerDto);

    void activateTrainer(String username);

    void deactivateTrainer(String username);

    void deleteTrainer(String username);

    ResponseEntity<ResponseRegistrationDto> registerTrainer(TrainerRegistrationDto request);
}
