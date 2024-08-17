package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerResponse;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TrainerMapper {
    TrainerProfile toDto(Trainer trainer);

    Trainer toEntity(TrainerUpdateRequest trainerDTO);
}
