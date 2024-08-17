package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.dto.ResponseRegistrationDto;
import com.epam.esm.gym.dto.TraineeDto;
import com.epam.esm.gym.dto.TraineeRequestRegistrationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TraineeMapper {
    TraineeDto toDto(Trainee trainee);

    ResponseRegistrationDto toResponse(Trainee trainee);

    Trainee toEntity(TraineeDto traineeDTO);

    Trainee toEntity(TraineeRequestRegistrationDto dto);
}
