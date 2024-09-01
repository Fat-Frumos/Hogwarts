package com.epam.esm.gym.web.provider.trainee;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class TraineeTrainerNameArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of("Albus Dumbledore", "Harry.Potter", "Defense Against the Dark Arts"),
                Arguments.of("Remus.Lupin", "Hermione.Granger", "Care of Magical Creatures"),
                Arguments.of("Severus.Snape", "Draco.Malfoy", "Potions")
        );
    }
}