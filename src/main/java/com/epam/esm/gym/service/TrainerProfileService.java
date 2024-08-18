package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TrainerDao;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import com.epam.esm.gym.mapper.TrainerMapper;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class TrainerProfileService implements TrainerService {

    private final UserProfileService userService;
    private final TrainerDao dao;
    private final TrainerMapper mapper;

    @Override
    public ProfileResponse registerTrainer(TrainerRequest dto) {
        TrainerProfile profile = userService.saveTrainer(dto);
        Trainer trainer = dao.save(mapper.toEntity(profile));
        return mapper.toProfileDto(trainer.getUser());
    }

    @Override
    public Optional<TrainerProfile> getTrainerByUsername(String username) {
        return dao.findByUsername(username).map(mapper::toDto);
    }

    @Override
    public void changeTrainerPassword(String username, String newPassword) {
        Optional<Trainer> trainerOptional = dao.findByUsername(username);
        trainerOptional.ifPresent(dao::updateTrainer);
    }

    @Override
    public void activateTrainer(String username) {
    }

    @Override
    public void deactivateTrainer(String username) {
        Optional<Trainer> trainerOptional = dao.findByUsername(username);
        trainerOptional.ifPresent(dao::delete);
    }

    @Override
    public void deleteTrainer(String username) {
        Optional<Trainer> trainerOptional = dao.findByUsername(username);
        trainerOptional.ifPresent(dao::delete);
    }

    @Override
    public TrainerProfile getTrainer(String username) {
        return null;
    }

    @Override
    public TrainerProfile updateTrainer(String username, TrainerUpdateRequest request) {
        Trainer trainer = mapper.toEntity(request);
        Trainer updated = dao.updateTrainer(trainer);
        return mapper.toDto(updated);
    }

    @Override
    public List<TrainerProfile> getNotAssigned(String traineeUsername) {
        return null;
    }

    @Override
    public void activateDeactivateProfile(String username, Boolean isActive) {

    }
}
