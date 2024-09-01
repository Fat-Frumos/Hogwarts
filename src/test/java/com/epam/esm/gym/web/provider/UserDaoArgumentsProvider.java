package com.epam.esm.gym.web.provider;

import com.epam.esm.gym.domain.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class UserDaoArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        User activeUser = User.builder()
                .id(1)
                .firstName("Harry")
                .lastName("Potter")
                .username("Harry.Potter")
                .password("password123")
                .active(true)
//                .permission(RoleType.TRAINER)
                .build();

        User inactiveUser = User.builder()
                .id(2)
                .firstName("Ron")
                .lastName("Weasley")
                .username("Ron.Weasley")
                .password("password456")
                .active(false)
//                .permission(RoleType.TRAINEE)
                .build();

        return Stream.of(
                Arguments.of(activeUser, "Harry.Potter"),
                Arguments.of(inactiveUser, "Ron.Weasley"),
                Arguments.of(new User(), "Hermione.Granger")
        );
    }
}
