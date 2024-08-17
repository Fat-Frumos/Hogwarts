package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TraineeMapper {
    TraineeProfile toDto(Trainee trainee);

    ProfileResponse toResponse(Trainee trainee);

    Trainee toEntity(TraineeProfile traineeResponse);

    Trainee toEntity(TraineeRequest dto);
}
