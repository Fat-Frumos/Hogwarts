package com.epam.esm.gym.workload.provider;

import com.epam.esm.gym.jms.dto.TrainerProfile;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides a stream of arguments for parameterized tests involving TrainerProfile instances.
 * This class implements the ArgumentsProvider interface to supply different sets of trainer profiles
 * for testing the WeeklyReportService.
 */
public class TrainerProfileListProvider implements ArgumentsProvider {
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

        List<TrainerProfile> trainersList = List.of(trainer1, trainer2);

        return Stream.of(
                Arguments.of(trainersList)
        );
    }
}
