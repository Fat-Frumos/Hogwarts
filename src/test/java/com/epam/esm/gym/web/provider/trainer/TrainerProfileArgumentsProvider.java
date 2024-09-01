package com.epam.esm.gym.web.provider.trainer;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.domain.TrainingType;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

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
