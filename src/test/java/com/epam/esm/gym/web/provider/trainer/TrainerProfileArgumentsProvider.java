package com.epam.esm.gym.web.provider.trainer;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;


/**
 * Provides arguments for testing scenarios related to trainer profiles.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply various sets of input data
 * for test cases that involve trainer profiles. The arguments provided simulate different
 * scenarios related to managing or querying trainer profiles, including valid and invalid
 * profiles.</p>
 *
 * <p>Each set of arguments can represent different states or attributes of a trainer profile,
 * such as existing profiles with varying details, as well as edge cases. This ensures thorough
 * testing of the functionalities that involve trainer profile management.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class TrainerProfileArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        TrainerRequest request = TrainerRequest.builder()
                .firstName("Remus")
                .lastName("Lupin")
                .specialization(Specialization.TRANSFIGURATION.name())
                .build();

        TrainingType transfiguration = TrainingType.builder()
                .trainingType(Specialization.TRANSFIGURATION)
                .build();

        TrainerProfile profile = TrainerProfile.builder()
                .username("Remus.Lupin")
                .firstName("Remus")
                .lastName("Lupin")
                .specialization(transfiguration)
                .active(true)
                .build();

        return Stream.of(
                Arguments.of("Remus.Lupin", profile, request)
        );
    }
}
