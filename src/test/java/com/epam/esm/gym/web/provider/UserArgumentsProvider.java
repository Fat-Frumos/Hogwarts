package com.epam.esm.gym.web.provider;

import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.UserProfile;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class UserArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        User harry = User.builder()
                .id(1)
                .firstName("Harry")
                .lastName("Potter")
                .username("Harry.Potter")
                .password("password123")
                .active(true)
                .permission(RoleType.TRAINER)
                .build();

        UserProfile profileHarry = new UserProfile(1L, "Harry", "Potter", "Harry.Potter", "password123", true);
        User hermione = User.builder()
                .id(2)
                .firstName("Hermione")
                .lastName("Granger")
                .username("Hermione.Granger")
                .password("password456")
                .active(true)
                .permission(RoleType.TRAINER)
                .build();

        UserProfile profileHermione = new UserProfile(2L, "Hermione", "Granger", "Hermione.Granger", "password456", true);

        User ron = User.builder()
                .id(2)
                .firstName("Ron")
                .lastName("Weasley")
                .username("Ron.Weasley")
                .password("password789")
                .active(true)
                .permission(RoleType.TRAINER)
                .build();

        UserProfile profileRon = new UserProfile(3L, "Ron", "Weasley", "Ron.Weasley", "password789", true);

        return Stream.of(
                Arguments.of("Harry.Potter", harry, profileHarry),
                Arguments.of("Hermione.Granger", hermione, profileHermione),
                Arguments.of("Ron.Weasley", ron, profileRon)
        );
    }
}
