package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeDto;
import com.epam.esm.gym.dto.trainee.TraineeRegistrationRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TraineeMapper {
    TraineeDto toDto(Trainee trainee);

    ProfileResponse toResponse(Trainee trainee);

    Trainee toEntity(TraineeDto traineeDTO);

    Trainee toEntity(TraineeRegistrationRequestDto dto);
}
