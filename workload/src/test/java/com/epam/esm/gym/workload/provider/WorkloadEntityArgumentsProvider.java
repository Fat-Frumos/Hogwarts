package com.epam.esm.gym.workload.provider;

import com.epam.esm.gym.workload.entity.TrainerWorkload;
import com.epam.esm.gym.workload.entity.TrainingWorkload;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDate;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * ArgumentsProvider for providing various Workload instances for testing.
 * This provider supplies different configurations of Workload to ensure
 * that different scenarios are tested during unit tests.
 */
public class WorkloadEntityArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        TrainingWorkload potions = TrainingWorkload.builder()
                .trainingName("Potions Class")
                .trainingType("Potions")
                .trainingDate(LocalDate.of(2024, 2, 2))
                .trainingDuration(60L)
                .build();

        TrainingWorkload defense = TrainingWorkload.builder()
                .trainingName("Defense Against the Dark Arts")
                .trainingType("Defense")
                .trainingDate(LocalDate.of(2024, 2, 2))
                .trainingDuration(45L)
                .build();

        TrainerWorkload potter = TrainerWorkload.builder()
                .active(true)
                .trainings(Collections.singletonList(potions))
                .build();

        TrainerWorkload granger = TrainerWorkload.builder()
                .active(true)
                .trainings(Collections.singletonList(defense))
                .build();

        return Stream.of(
                Arguments.of(potter),
                Arguments.of(granger)
        );
    }
}
