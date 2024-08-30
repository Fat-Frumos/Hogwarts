package com.epam.esm.gym.web.provider.trainee;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import java.util.stream.Stream;

public class NotFoundTraineeProfileArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of("Bob", ResponseEntity.notFound().build()),
                Arguments.of("Alice", ResponseEntity.notFound().build())
        );
    }
}
