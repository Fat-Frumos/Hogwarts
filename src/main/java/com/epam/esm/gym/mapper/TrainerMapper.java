package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.dto.trainer.TrainerDto;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TrainerMapper {
    TrainerDto toDto(Trainer trainer);

    Trainer toEntity(TrainerUpdateRequest trainerDTO);
}
