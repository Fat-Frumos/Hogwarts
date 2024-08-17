package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TrainerDao;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerDto;
import com.epam.esm.gym.dto.trainer.TrainerRegistrationDto;
import com.epam.esm.gym.dto.trainer.TrainerResponse;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import com.epam.esm.gym.mapper.TrainerMapper;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DefaultTrainerService implements TrainerService {

    private final TrainerDao trainerDao;
    private final TrainerMapper mapper;

    public DefaultTrainerService(TrainerDao trainerDao, TrainerMapper trainerMapper) {
        this.trainerDao = trainerDao;
        this.mapper = trainerMapper;
    }

    @Override
    public Optional<TrainerDto> getTrainerByUsername(String username) {
        return trainerDao.findByUsername(username).map(mapper::toDto);
    }

    @Override
    public void changeTrainerPassword(String username, String newPassword) {
        Optional<Trainer> trainerOptional = trainerDao.findByUsername(username);
        trainerOptional.ifPresent(trainerDao::updateTrainer);
    }

    @Override
    public void activateTrainer(String username) {
        Optional<Trainer> trainerOptional = trainerDao.findByUsername(username);
    }

    @Override
    public void deactivateTrainer(String username) {
        Optional<Trainer> trainerOptional = trainerDao.findByUsername(username);
        trainerOptional.ifPresent(trainerDao::delete);
    }

    @Override
    public void deleteTrainer(String username) {
        Optional<Trainer> trainerOptional = trainerDao.findByUsername(username);
        trainerOptional.ifPresent(trainerDao::delete);
    }

    @Override
    public ProfileResponse registerTrainer(TrainerRegistrationDto request) {
        return null;
    }

    @Override
    public TrainerResponse getTrainer(String username) {
        return null;
    }

    @Override
    public TrainerDto updateTrainer(String username, TrainerUpdateRequest request) {
        Trainer trainer = mapper.toEntity(request);
        Trainer updated = trainerDao.updateTrainer(trainer);
        return mapper.toDto(updated);
    }

    @Override
    public List<TrainerResponse> getNotAssigned(String traineeUsername) {
        return null;
    }

    @Override
    public void activateDeactivateProfile(String username, Boolean isActive) {

    }
}
