package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TrainerMapper {
    TrainerProfile toDto(Trainer trainer);

    Trainer toEntity(TrainerProfile profile);

    Trainer toEntity(TrainerUpdateRequest dto);

    Trainee toEntity(TraineeProfile trainee);

    ProfileResponse toProfileDto(User user);

    List<TrainerProfile> toDtos(List<Trainer> notAssigned);
}
