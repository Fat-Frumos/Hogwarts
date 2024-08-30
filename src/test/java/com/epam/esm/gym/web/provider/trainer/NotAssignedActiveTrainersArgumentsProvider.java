package com.epam.esm.gym.web.provider.trainer;

import com.epam.esm.gym.dto.trainer.TrainerProfile;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Stream;

public class NotAssignedActiveTrainersArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        TrainerProfile trainer1 = TrainerProfile.builder()
                .username("Minerva.McGonagall")
                .firstName("Minerva")
                .lastName("McGonagall")
                .build();

        TrainerProfile trainer2 = TrainerProfile.builder()
                .username("Remus.Lupin")
                .firstName("Remus")
                .lastName("Lupin")
                .build();

        return Stream.of(
                Arguments.of(
                        "Harry.Potter",
                        ResponseEntity.ok(List.of(trainer1, trainer2))
                )
        );
    }
}
