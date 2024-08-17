package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TrainerDao;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.dto.ResponseRegistrationDto;
import com.epam.esm.gym.dto.TrainerDto;
import com.epam.esm.gym.dto.TrainerRegistrationDto;
import com.epam.esm.gym.mapper.TrainerMapper;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
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
    public void updateTrainer(TrainerDto trainerDto) {
        Trainer trainer = mapper.toEntity(trainerDto);
        trainerDao.updateTrainer(trainer);
    }

    @Override
    public void activateTrainer(String username) {
        Optional<Trainer> trainerOptional = trainerDao.findByUsername(username);
//        trainerOptional.ifPresent(trainer -> trainerDao.activateTrainer(trainer.getId()));
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

    public ResponseEntity<ResponseRegistrationDto> registerTrainer(TrainerRegistrationDto request) {
        return null;
    }
}
