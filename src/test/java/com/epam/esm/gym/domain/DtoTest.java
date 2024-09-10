package com.epam.esm.gym.domain;

import com.epam.esm.gym.dto.auth.MessageResponse;
import com.epam.esm.gym.dto.auth.RegisterRequest;
import com.epam.esm.gym.dto.auth.UserPrincipal;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.PostTraineeRequest;
import com.epam.esm.gym.dto.trainee.TraineeProfileResponseResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.dto.training.TrainingTypeDto;
import com.epam.esm.gym.security.service.JwtProperties;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DtoTest {

    @Test
    void testEqualsAndHashCode() {
        JwtProperties jwtProperties1 = new JwtProperties();
        jwtProperties1.setIssuer("Issuer1");
        jwtProperties1.setSecret("Secret1");
        jwtProperties1.setAccess(3600L);
        jwtProperties1.setRefresh(7200L);

        JwtProperties jwtProperties2 = new JwtProperties();
        jwtProperties2.setIssuer("Issuer1");
        jwtProperties2.setSecret("Secret1");
        jwtProperties2.setAccess(3600L);
        jwtProperties2.setRefresh(7200L);

        JwtProperties jwtProperties3 = new JwtProperties();
        jwtProperties3.setIssuer("Issuer2");
        jwtProperties3.setSecret("Secret2");
        jwtProperties3.setAccess(7200L);
        jwtProperties3.setRefresh(14400L);

        assertEquals(jwtProperties1, jwtProperties2);
        assertNotEquals(jwtProperties1, jwtProperties3);
        assertEquals(jwtProperties1.hashCode(), jwtProperties2.hashCode());
        assertNotEquals(jwtProperties1.hashCode(), jwtProperties3.hashCode());
    }

    @Test
    void testGettersAndSetters() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setIssuer("Issuer1");
        jwtProperties.setSecret("Secret1");
        jwtProperties.setAccess(3600L);
        jwtProperties.setRefresh(7200L);

        assertEquals("Issuer1", jwtProperties.getIssuer());
        assertEquals("Secret1", jwtProperties.getSecret());
        assertEquals(3600L, jwtProperties.getAccess());
        assertEquals(7200L, jwtProperties.getRefresh());
    }

    @Test
    void testRegisterRequest() {
        RegisterRequest request1 = new RegisterRequest(
                "HarryPotter", "Harry", "Potter", "password123");
        RegisterRequest request2 = new RegisterRequest(
                "HarryPotter", "Harry", "Potter", "password123");
        RegisterRequest request3 = new RegisterRequest(
                "HermioneGranger", "Hermione", "Granger", "password456");

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
        assertEquals("HarryPotter", request1.getUsername());
        assertEquals("Harry", request1.getFirstName());
        assertEquals("Potter", request1.getLastName());
        assertEquals("password123", request1.getPassword());
    }

    @Test
    void testProfileResponse() {
        ProfileResponse login1 = new ProfileResponse("HarryPotter", "password123");
        ProfileResponse login2 = new ProfileResponse("HarryPotter", "password123");
        ProfileResponse login3 = new ProfileResponse("HermioneGranger", "password456");

        assertEquals(login1, login2);
        assertNotEquals(login1, login3);
        assertEquals(login1.hashCode(), login2.hashCode());
        assertNotEquals(login1.hashCode(), login3.hashCode());
        assertEquals("HarryPotter", login1.getUsername());
        assertEquals("password123", login1.getPassword());
    }

    @Test
    void testMessageResponse() {
        MessageResponse response1 = new MessageResponse("Success");
        MessageResponse response2 = new MessageResponse("Success");
        MessageResponse response3 = new MessageResponse("Error");

        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
        assertEquals("Success", response1.getMessage());
    }

    @Test
    void testLoginResponse() {
        ProfileResponse profile1 = new ProfileResponse("HarryPotter", "password123");
        ProfileResponse profile2 = new ProfileResponse("HarryPotter", "password123");
        ProfileResponse profile3 = new ProfileResponse("HermioneGranger", "password456");

        assertEquals(profile1, profile2);
        assertNotEquals(profile1, profile3);
        assertEquals(profile1.hashCode(), profile2.hashCode());
        assertNotEquals(profile1.hashCode(), profile3.hashCode());
        assertEquals("HarryPotter", profile1.getUsername());
        assertEquals("password123", profile1.getPassword());
    }

    @Test
    void testTraineeProfile() {
        TrainerProfile trainer = new TrainerProfile(
                "AlbusDumbledore", "Albus", "Dumbledore", List.of());
        TraineeProfileResponseResponse trainee1 = new TraineeProfileResponseResponse(
                "Harry", "Potter", "HarryPotter", "Hogwarts", true,
                LocalDate.of(1980, 7, 31), List.of(trainer));
        TraineeProfileResponseResponse trainee2 = new TraineeProfileResponseResponse(
                "Harry", "Potter", "HarryPotter", "Hogwarts", true,
                LocalDate.of(1980, 7, 31), List.of(trainer));
        TraineeProfileResponseResponse trainee3 = new TraineeProfileResponseResponse(
                "Hermione", "Granger", "HermioneGranger", "Hogwarts", true,
                LocalDate.of(1979, 9, 19), List.of());

        assertEquals(trainee1, trainee2);
        assertNotEquals(trainee1, trainee3);
        assertEquals(trainee1.hashCode(), trainee2.hashCode());
        assertNotEquals(trainee1.hashCode(), trainee3.hashCode());
        assertEquals("Harry", trainee1.getFirstName());
        assertEquals("Potter", trainee1.getLastName());
        assertEquals("HarryPotter", trainee1.getUsername());
        assertEquals("Hogwarts", trainee1.getAddress());
        assertTrue(trainee1.getActive());
        assertEquals(LocalDate.of(1980, 7, 31), trainee1.getDateOfBirth());
        assertEquals(1, trainee1.getTrainers().size());
    }

    @Test
    void testTraineeRequest() {
        PostTraineeRequest request1 = new PostTraineeRequest(
                "Harry", "Potter", LocalDate.of(1980, 7, 31), "Hogwarts");
        PostTraineeRequest request2 = new PostTraineeRequest(
                "Harry", "Potter", LocalDate.of(1980, 7, 31), "Hogwarts");
        PostTraineeRequest request3 = new PostTraineeRequest(
                "Hermione", "Granger", LocalDate.of(1979, 9, 19), "Hogwarts");

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
        assertEquals("Harry", request1.getFirstName());
        assertEquals("Potter", request1.getLastName());
        assertEquals(LocalDate.of(1980, 7, 31), request1.getDateOfBirth());
        assertEquals("Hogwarts", request1.getAddress());
    }

    @Test
    void testTrainerProfile() {
        TraineeProfileResponseResponse trainee = new TraineeProfileResponseResponse(
                "Harry", "Potter", "HarryPotter", "Hogwarts",
                true, LocalDate.of(1980, 7, 31), List.of());
        TrainerProfile trainer1 = new TrainerProfile(
                "AlbusDumbledore", "Albus", "Dumbledore", List.of(trainee));
        TrainerProfile trainer2 = new TrainerProfile(
                "AlbusDumbledore", "Albus", "Dumbledore", List.of(trainee));
        TrainerProfile trainer3 = new TrainerProfile(
                "SeverusSnape", "Severus", "Snape", List.of());

        assertEquals(trainer1, trainer2);
        assertNotEquals(trainer1, trainer3);
        assertEquals(trainer1.hashCode(), trainer2.hashCode());
        assertNotEquals(trainer1.hashCode(), trainer3.hashCode());
        assertEquals("AlbusDumbledore", trainer1.getUsername());
        assertEquals("Albus", trainer1.getFirstName());
        assertEquals("Dumbledore", trainer1.getLastName());
        assertEquals(1, trainer1.getTrainees().size());
    }

    @Test
    void testTrainingRequest() {
        TrainingRequest request1 = new TrainingRequest(
                "HarryPotter", "AlbusDumbledore",
                "DefenseAgainstTheDarkArts", "2024-09-03", 90);
        TrainingRequest request2 = new TrainingRequest("HarryPotter", "AlbusDumbledore",
                "DefenseAgainstTheDarkArts", "2024-09-03", 90);
        TrainingRequest request3 = new TrainingRequest(
                "HermioneGranger", "SeverusSnape",
                "POISON", "2024-09-04", 120);

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
        assertEquals("HarryPotter", request1.getTraineeUsername());
        assertEquals("AlbusDumbledore", request1.getTrainerUsername());
        assertEquals("DefenseAgainstTheDarkArts", request1.getTrainingName());
        assertEquals("2024-09-03", request1.getTrainingDate());
        assertEquals(90, request1.getTrainingDuration());
    }

    @Test
    void testTrainingResponse() {
        TrainingResponse response1 = new TrainingResponse(
                "Albus Dumbledore", "DefenseAgainstTheDarkArts", "Magic",
                90, LocalDate.of(2024, 9, 3));
        TrainingResponse response2 = new TrainingResponse(
                "Albus Dumbledore", "DefenseAgainstTheDarkArts", "Magic",
                90, LocalDate.of(2024, 9, 3));
        TrainingResponse response3 = new TrainingResponse(
                "Severus Snape", "POISON", "Magic",
                120, LocalDate.of(2024, 9, 4));

        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
        assertEquals("Albus Dumbledore", response1.getTrainerName());
        assertEquals("DefenseAgainstTheDarkArts", response1.getTrainingName());
        assertEquals("Magic", response1.getTrainingType());
        assertEquals(90, response1.getTrainingDuration());
        assertEquals(LocalDate.of(2024, 9, 3), response1.getTrainingDate());
    }

    @Test
    void testTrainingTypeResponse() {
        TrainingTypeDto type1 = new TrainingTypeDto(Specialization.CARDIO, 1L);
        TrainingTypeDto type2 = new TrainingTypeDto(Specialization.CARDIO, 1L);
        TrainingTypeDto type3 = new TrainingTypeDto(Specialization.POISON, 2L);

        assertEquals(type1, type2);
        assertNotEquals(type1, type3);
        assertEquals(type1.hashCode(), type2.hashCode());
        assertNotEquals(type1.hashCode(), type3.hashCode());
        assertEquals(Specialization.CARDIO, type1.getSpecialization());
        assertEquals(1L, type1.getTrainingTypeId());
    }

    @Test
    void testTrainingSession() {
        Trainer trainer = new Trainer();
        Training training = new Training();
        LocalDateTime start = LocalDateTime.of(2024, 9, 3, 14, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 3, 15, 30);
        TrainingSession session1 = new TrainingSession(1L, trainer, training, start, end);
        Duration expectedDuration = Duration.ofHours(1).plusMinutes(30);
        assertEquals(expectedDuration, session1.getDuration());
        assertEquals(1L, session1.getId());
        assertEquals(trainer, session1.getTrainer());
        assertEquals(training, session1.getTraining());
        assertEquals(start, session1.getStartTime());
        assertEquals(end, session1.getEndTime());

        LocalDateTime start30Days = LocalDateTime.of(2024, 8, 4, 14, 0);
        LocalDateTime end30Days = LocalDateTime.of(2024, 9, 3, 14, 0);
        TrainingSession session2 = new TrainingSession(2L, trainer, training, start30Days, end30Days);
        Duration expected30DaysDuration = Duration.ofDays(30);
        assertEquals(expected30DaysDuration, session2.getDuration());
        LocalDateTime endDifferent = LocalDateTime.of(2024, 9, 3, 16, 0);
        TrainingSession session3 = new TrainingSession(3L, trainer, training, start, endDifferent);
        assertNotEquals(session1, session2);
        assertNotEquals(session1, session3);
        assertNotEquals(session2, session3);
        assertEquals(session1.hashCode(), new TrainingSession(1L, trainer, training, start, end).hashCode());
        assertNotEquals(session1.hashCode(), session2.hashCode());
        assertNotEquals(session1.hashCode(), session3.hashCode());
    }

    @Test
    void testSecurityUser() {
        User user = Mockito.mock(User.class);
        RoleType permission = Mockito.mock(RoleType.class);
        List<SimpleGrantedAuthority> authorities = Collections.emptyList();
        Mockito.when(permission.getGrantedAuthorities()).thenReturn(authorities);
        Mockito.when(user.getPassword()).thenReturn("password123");
        Mockito.when(user.getUsername()).thenReturn("harry.potter");
        UserPrincipal securityUser = UserPrincipal.builder().user(user).build();
        assertEquals("password123", securityUser.getPassword());
        assertEquals("harry.potter", securityUser.getUsername());
    }
}
