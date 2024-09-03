package com.epam.esm.gym.web.provider;

import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Provides arguments for parameterized tests related to User DAO operations.
 *
 * <p>This class implements the {@link ArgumentsProvider} interface to supply a set of test
 * arguments for methods that interact with the User DAO. These arguments cover a range of scenarios
 * for user operations including finding, saving, and deleting users.</p>
 *
 * <p>Test scenarios include both valid and invalid user data to ensure comprehensive coverage
 * and robustness of the DAO methods. This allows testing various cases to confirm that the DAO
 * behaves correctly under different conditions.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
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
                .permission(RoleType.ROLE_TRAINER)
                .build();

        User inactiveUser = User.builder()
                .id(2)
                .firstName("Ron")
                .lastName("Weasley")
                .username("Ron.Weasley")
                .password("password456")
                .active(false)
                .permission(RoleType.ROLE_TRAINEE)
                .build();

        return Stream.of(
                Arguments.of(activeUser, "Harry.Potter"),
                Arguments.of(inactiveUser, "Ron.Weasley"),
                Arguments.of(new User(), "Hermione.Granger")
        );
    }
}
