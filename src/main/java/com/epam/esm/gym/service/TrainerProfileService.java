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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<ProfileResponse> registerTrainer(TrainerRequest dto) {
        TrainerProfile profile = userService.saveTrainer(dto);
        Trainer trainer = dao.save(mapper.toEntity(profile));
        ProfileResponse response = mapper.toProfileDto(trainer.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public void changeTrainerPassword(ProfileRequest request) {
        userService.changePassword(request);
    }

    @Override
    public void deleteTrainer(String username) {
        dao.delete(getTrainer(username));
    }

    @Override
    public ResponseEntity<TrainerProfile> getTrainerProfileByName(String username) {
        return ResponseEntity.ok(mapper.toDto(getTrainer(username)));
    }

    @Override
    public ResponseEntity<TrainerProfile> updateTrainer(
            String username, TrainerUpdateRequest request) {
        Trainer trainer = mapper.toEntity(request);
        TrainerProfile profile = mapper.toDto(dao.update(trainer));
        return ResponseEntity.ok(profile);
    }

    @Override
    public ResponseEntity<List<TrainerProfile>> getNotAssigned(String username) {
        List<Trainer> notAssigned = dao.findNotAssigned(username);
        return ResponseEntity.ok(mapper.toDtos(notAssigned));
    }

    @Override
    public ResponseEntity<Void> activateDeactivateProfile(String username, Boolean active) {
        dao.activateTrainer(username, active);
        return ResponseEntity.ok().build();
    }

    public Trainer getTrainer(String username) {
        return dao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(username));
    }

    @Override
    public ResponseEntity<List<TrainerProfile>> findAll() {
        return ResponseEntity.ok(mapper.toDtos(dao.findAll()));
    }

    @Override
    public void assignTraineeToTrainer(String traineeUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        dao.assignTraineeToTrainer(authentication.getName(), traineeUsername);
    }
}
