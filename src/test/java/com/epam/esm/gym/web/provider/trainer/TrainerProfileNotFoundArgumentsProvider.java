package com.epam.esm.gym.web.provider.trainer;

import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

public class TrainerProfileNotFoundArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        TrainerRequest request = TrainerRequest.builder()
                .firstName("Alice")
                .lastName("Bob")
                .specialization(Specialization.DEFENSE.name())
                .build();

        MessageResponse response = MessageResponse.builder()
                .message("Trainer not found")
                .status(HttpStatus.NOT_FOUND)
                .build();

        return Stream.of(
                Arguments.of(request, ResponseEntity.status(HttpStatus.NOT_FOUND).body(response))
        );
    }
}
