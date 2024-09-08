package com.epam.esm.gym.web.provider.trainee;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * Provides arguments for testing scenarios involving trainee and trainer names.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply input data specifically for
 * test cases that focus on trainee and trainer names. It is used to verify the system's behavior
 * when processing or handling different trainee and trainer names.</p>
 *
 * <p>The provided arguments cover a range of names for trainees and trainers to test the
 * system's ability to handle various naming scenarios, including valid and edge cases.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class TraineeTrainerNameArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of("Albus Dumbledore", "Harry.Potter", "Defense Against the Dark Arts"),
                Arguments.of("Remus.Lupin", "Hermione.Granger", "Care of Magical Creatures"),
                Arguments.of("Severus.Snape", "Draco.Malfoy", "POISON")
        );
    }
}
