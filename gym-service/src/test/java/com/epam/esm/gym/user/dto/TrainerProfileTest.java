package com.epam.esm.gym.user.dto;

import com.epam.esm.gym.jms.dto.TrainerProfile;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.user.dto.trainee.TraineeProfileResponse;
import com.epam.esm.gym.user.provider.TraineeProfileResponseProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the TrainerProfile class.
 * This class contains parameterized tests to validate the behavior of the TrainerProfile constructor,
 * builder, and string representation methods.
 */
public class TrainerProfileTest {
    List<TrainingResponse> trainingResponses;
    final String trainerName = "Remus.Lupin";
    @BeforeEach
    void setUp() {
        trainingResponses = List.of(
                TrainingResponse.builder()
                        .trainerName(trainerName)
                        .trainingDate(LocalDate.of(2023, 1, 1))
                        .trainingDuration(60)
                        .build()
        );
    }

    @Test
    void superBuilderShouldInitializeFieldsCorrectly() {
        TrainerProfile trainerProfile = new TrainerProfile(
                "Hermione.Granger", "Hermione", "Granger", true, trainingResponses);
        assertThat(trainerProfile.username()).isEqualTo("Hermione.Granger");
        assertThat(trainerProfile.firstName()).isEqualTo("Hermione");
        assertThat(trainerProfile.lastName()).isEqualTo("Granger");
        assertThat(trainerProfile.trainings()).isEqualTo(trainingResponses);
        assertThat(trainerProfile.active()).isEqualTo(true);
    }

    @Test
    void toStringShouldReturnCorrectString() {
        TrainerProfile trainerProfile = new TrainerProfile(
                "Hermione.Granger", "Hermione", "Granger", true, trainingResponses);
        String expected = "TrainerProfile[username=Hermione.Granger, firstName=Hermione, lastName=Granger, " +
                "active=true, trainings=[TrainingResponse(trainerName=Remus.Lupin, trainingName=null, " +
                "trainingType=null, trainingDuration=60, trainingDate=2023-01-01)]]";
        assertThat(trainerProfile.toString()).isEqualTo(expected);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeProfileResponseProvider.class)
    void constructor_shouldInitializeFieldsCorrectly(List<TraineeProfileResponse> trainees) {
        TrainerProfile trainerProfile = new TrainerProfile("Hermione.Granger", "Hermione",
                "Granger", true, trainingResponses);
        assertThat(trainerProfile.username()).isEqualTo("Hermione.Granger");
        assertThat(trainerProfile.firstName()).isEqualTo("Hermione");
        assertThat(trainerProfile.lastName()).isEqualTo("Granger");
        assertThat(trainerProfile.active()).isTrue();
        assertThat(trainerProfile.trainings()).isEqualTo(trainingResponses);
    }

    @ParameterizedTest
    @ArgumentsSource(TraineeProfileResponseProvider.class)
    void equals_shouldReturnTrueForSameValues(List<TraineeProfileResponse> trainees) {
        TrainerProfile trainerProfile1 = new TrainerProfile(
                "Hermione.Granger", "Hermione", "Granger", true, trainingResponses);
        TrainerProfile trainerProfile2 = new TrainerProfile(
                "Hermione.Granger", "Hermione", "Granger", true, trainingResponses);
        assertThat(trainerProfile1).isEqualTo(trainerProfile2);
    }

    @Test
    void equals_shouldReturnFalseForDifferentValues() {
        TrainerProfile trainerProfile1 = new TrainerProfile(
                "Hermione.Granger", "Hermione", "Granger", true, trainingResponses);
        TrainerProfile trainerProfile2 = new TrainerProfile(
                "Harry.Potter", "Harry", "Potter", true, trainingResponses);
        assertThat(trainerProfile1).isNotEqualTo(trainerProfile2);
    }

    @Test
    void hashCode_shouldReturnSameHashCodeForEqualObjects() {
        TrainerProfile trainerProfile1 = new TrainerProfile(
                "Hermione.Granger", "Hermione", "Granger", true, trainingResponses);
        TrainerProfile trainerProfile2 = new TrainerProfile(
                "Hermione.Granger", "Hermione", "Granger", true, trainingResponses);
        assertThat(trainerProfile1.hashCode()).isEqualTo(trainerProfile2.hashCode());
    }

    @Test
    void hashCode_shouldReturnDifferentHashCodeForDifferentObjects() {
        TrainerProfile trainerProfile1 = new TrainerProfile(
                "Hermione.Granger", "Hermione", "Granger", true, trainingResponses);
        TrainerProfile trainerProfile2 = new TrainerProfile(
                "Harry.Potter", "Harry", "Potter", true, trainingResponses);
        assertThat(trainerProfile1.hashCode()).isNotEqualTo(trainerProfile2.hashCode());
    }
}
