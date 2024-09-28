package com.epam.esm.gym.user.provider;

import com.epam.esm.gym.user.entity.RoleType;
import com.epam.esm.gym.user.entity.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Provides arguments for parameterized tests related to the User Data Access Object (DAO).
 *
 * <p>This class implements the {@link ArgumentsProvider} interface to supply test arguments
 * specifically related to DAO operations for the {@link User} entity. It generates a stream of
 * {@link Arguments} representing various scenarios for testing DAO methods such as finding,
 * saving, and deleting users.</p>
 *
 * <p>Using this provider, you can create tests that cover different cases of interacting with
 * the User DAO, including valid and invalid user data, to ensure robust DAO behavior and data
 * integrity.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
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
                .permission(RoleType.ROLE_TRAINER)
                .build();

        User hermione = User.builder()
                .id(2)
                .firstName("Hermione")
                .lastName("Granger")
                .username("Hermione.Granger")
                .password("password456")
                .active(true)
                .build();

        User ron = User.builder()
                .id(2)
                .firstName("Ron")
                .lastName("Weasley")
                .username("Ron.Weasley")
                .password("password789")
                .active(true)
                .permission(RoleType.ROLE_TRAINER)
                .build();

        return Stream.of(
                Arguments.of("Harry.Potter", harry),
                Arguments.of("Hermione.Granger", hermione),
                Arguments.of("Ron.Weasley", ron)
        );
    }
}
