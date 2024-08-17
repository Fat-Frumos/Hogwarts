package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDTO(User user);

    User toEntity(UserDto userDTO);
}
