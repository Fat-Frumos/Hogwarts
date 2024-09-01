package com.epam.esm.gym.web.provider.trainee;

import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.stream.Stream;

public class TraineeRegistrationArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {

        TraineeProfile profile = TraineeProfile.builder()
                .firstName("Harry")
                .lastName("Potter")
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .active(true)
                .build();

        User harry = User.builder()
                .id(1)
                .firstName("Harry")
                .lastName("Potter")
                .username("Harry.Potter")
                .password("password123")
                .active(true)
                .permission(RoleType.ROLE_TRAINEE)
                .build();

        Trainee trainee = Trainee.builder()
                .id(1L)
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .user(harry)
                .trainings(new HashSet<>())
                .build();

        TraineeRequest request = TraineeRequest.builder()
                .firstName("Harry")
                .lastName("Potter")
                .dateOfBirth(LocalDate.parse("1980-07-31"))
                .address("Hogwarts")
                .build();

        return Stream.of(Arguments.of(request, ResponseEntity.ok(getProfileResponse()), trainee, profile)
        );
    }

    private ProfileResponse getProfileResponse() {
        return ProfileResponse.builder()
                .username("Harry.Potter")
                .password("generated_password")
                .build();
    }
}
