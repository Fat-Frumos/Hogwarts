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

/**
 * Provides arguments for testing trainee registration scenarios.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply various sets of input data
 * for test cases related to registering trainees. It covers different scenarios to
 * validate the system's response to valid and invalid registration requests for trainees.</p>
 *
 * <p>The provided arguments include different combinations of valid and invalid registration
 * details to ensure comprehensive testing of the registration functionality, including edge
 * cases and error handling.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
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
                .password("password123")
                .build();
    }
}
