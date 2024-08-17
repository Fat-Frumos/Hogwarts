package com.epam.esm.gym.mapper;

import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserProfile toDTO(User user);

    User toEntity(UserProfile userProfile);
}
