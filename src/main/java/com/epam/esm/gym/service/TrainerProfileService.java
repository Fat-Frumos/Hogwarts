package com.epam.esm.gym.service;

import com.epam.esm.gym.dao.TrainerDao;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import com.epam.esm.gym.mapper.TrainerMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TrainerProfileService implements TrainerService {

    private final UserProfileService userService;
    private final TrainerMapper mapper;
    private final TrainerDao dao;

    @Override
    public ProfileResponse registerTrainer(TrainerRequest dto) {
        TrainerProfile profile = userService.saveTrainer(dto);
        Trainer trainer = dao.save(mapper.toEntity(profile));
        return mapper.toProfileDto(trainer.getUser());
    }

    @Override
    public void changeTrainerPassword(ProfileRequest request) {
        userService.changePassword(request);
    }

    @Override
    public void activateTrainer(String username) {
        dao.activateTrainer(username, true);
    }

    @Override
    public void deactivateTrainer(String username) {
        dao.activateTrainer(username, false);
    }

    @Override
    public void deleteTrainer(String username) {
        dao.delete(getTrainer(username));
    }

    @Override
    public TrainerProfile getTrainerProfileByName(String username) {
        return mapper.toDto(getTrainer(username));
    }

    @Override
    public TrainerProfile updateTrainer(String username, TrainerUpdateRequest request) {
        Trainer trainer = mapper.toEntity(request);
        return mapper.toDto(dao.update(trainer));
    }

    @Override
    public List<TrainerProfile> getNotAssigned(String username) {
        List<Trainer> notAssigned = dao.findNotAssigned(username);
        return mapper.toDtos(notAssigned);
    }

    @Override
    public void activateDeactivateProfile(String username, Boolean active) {
        dao.activateTrainer(username, active);
    }

    public Trainer getTrainer(String username) {
        return dao.findByUserName(username)
                .orElseThrow(() -> new EntityNotFoundException(username));
    }
}
