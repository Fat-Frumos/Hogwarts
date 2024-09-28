package com.epam.esm.gym.user.service.profile;

import com.epam.esm.gym.user.dto.trainer.TrainerProfile;
import com.epam.esm.gym.user.entity.Trainer;
import com.epam.esm.gym.user.entity.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides arguments for parameterized tests related to trainers.
 * This class generates various scenarios for testing the functionality
 * associated with trainer-related operations.
 */
public class TrainerArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of(
                        List.of(
                                Trainer.builder().user(User.builder().username("trainer1").build()).build(),
                                Trainer.builder().user(User.builder().username("trainer2").build()).build()
                        ),
                        List.of(
                                TrainerProfile.builder().username("trainer1").build(),
                                TrainerProfile.builder().username("trainer2").build()
                        )
                )
        );
    }
}