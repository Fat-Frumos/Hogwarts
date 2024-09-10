package com.epam.esm.gym.web.provider.trainer;


import com.epam.esm.gym.domain.Specialization;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerUpdateRequest;
import com.epam.esm.gym.dto.training.TrainingTypeDto;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Stream;

/**
 * Provides arguments for testing scenarios related to updating a trainer.
 *
 * <p>This class implements {@link ArgumentsProvider} to supply various sets of input data
 * for test cases that involve updating trainer details. The provided arguments are used
 * to simulate different update scenarios and ensure the update functionality is properly
 * validated under various conditions.</p>
 *
 * <p>Each set of arguments can represent different states or configurations of the trainer
 * being updated, including valid and invalid inputs. This helps to verify the robustness
 * of the update functionality and its interaction with the underlying service layer.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class UpdateTrainerArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        TrainingTypeDto transfiguration = TrainingTypeDto.builder()
                .specialization(Specialization.TRANSFIGURATION)
                .build();

        TrainerUpdateRequest updateRequest = TrainerUpdateRequest.builder()
                .firstName("Minerva")
                .lastName("McGonagall")
                .active(true)
                .build();

        TrainerProfile updatedProfile = TrainerProfile.builder()
                .username("Minerva.McGonagall")
                .firstName("Minerva")
                .lastName("McGonagall")
                .specializations(List.of(transfiguration))
                .build();

        return Stream.of(
                Arguments.of("Minerva.McGonagall", updateRequest, ResponseEntity.ok(updatedProfile))
        );
    }
}
