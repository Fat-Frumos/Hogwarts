package com.epam.esm.gym.web.provider.trainer;

import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

public class UpdateTrainerNotFoundArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        TrainerUpdateRequest updateRequest = TrainerUpdateRequest.builder()
                .firstName("NonExistent")
                .lastName("Trainer")
                .build();

        return Stream.of(
                Arguments.of("NonExistentTrainer", updateRequest, ResponseEntity.status(HttpStatus.BAD_REQUEST).build())
        );
    }
}
