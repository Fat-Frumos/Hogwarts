package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.profile.UserProfile;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserProfile toDto(User user);

    ProfileResponse toProfile(User user);

    User toEntity(UserProfile userProfile);

    TraineeProfile toTraineeProfile(User user);

    TrainerProfile toTrainerProfile(User user);
}
