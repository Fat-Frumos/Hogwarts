package com.epam.esm.gym.web.provider.trainer;

import com.epam.esm.gym.domain.RoleType;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.domain.User;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Provides arguments for testing scenarios related to trainer requests.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply different sets of input data
 * for test cases involving requests related to trainers. The provided arguments simulate various
 * scenarios of creating, updating, or querying trainer information, helping to validate the
 * correctness of the corresponding request handling functionalities.</p>
 *
 * <p>Each set of arguments can represent different states or attributes of the trainer being
 * handled, such as valid and invalid requests, different fields, and values. This is useful
 * for ensuring the robustness and reliability of the system's ability to process trainer-related
 * requests correctly.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class TrainerRequestArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        User user = User.builder()
                .id(1)
                .firstName("Harry")
                .lastName("Potter")
                .username("Harry.Potter")
                .password("Password123")
                .active(true)
                .permission(RoleType.ROLE_TRAINER)
                .build();
        TrainerRequest request = TrainerRequest.builder().firstName("Harry").build();
        TrainerProfile profile = TrainerProfile.builder().username("Harry.Potter").build();
        Trainer trainer = Trainer.builder().user(user).build();
        ProfileResponse response = ProfileResponse.builder().username("Harry.Potter").build();
        return Stream.of(Arguments.of(trainer, profile, request, response));
    }
}
