package com.epam.esm.gym.web.provider;

import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.UserProfile;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class UserArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        User harry = new User(1, "Harry", "Potter", "Harry.Potter", "password123", true);
        UserProfile profileHarry = new UserProfile(1L, "Harry", "Potter", "Harry.Potter", "password123", true);

        User hermione = new User(2, "Hermione", "Granger", "Hermione.Granger", "password456", true);
        UserProfile profileHermione = new UserProfile(2L, "Hermione", "Granger", "Hermione.Granger", "password456", true);

        User ron = new User(3, "Ron", "Weasley", "Ron.Weasley", "password789", true);
        UserProfile profileRon = new UserProfile(3L, "Ron", "Weasley", "Ron.Weasley", "password789", true);

        return Stream.of(
                Arguments.of("Harry.Potter", harry, profileHarry),
                Arguments.of("Hermione.Granger", hermione, profileHermione),
                Arguments.of("Ron.Weasley", ron, profileRon)
        );
    }
}
