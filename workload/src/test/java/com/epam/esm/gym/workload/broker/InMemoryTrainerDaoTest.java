package com.epam.esm.gym.workload.broker;

import com.epam.esm.gym.jms.dto.ActionType;
import com.epam.esm.gym.jms.dto.TrainerProfile;
import com.epam.esm.gym.jms.dto.TrainerRequest;
import com.epam.esm.gym.jms.dto.TrainerStatus;
import com.epam.esm.gym.jms.dto.TrainerSummary;
import com.epam.esm.gym.jms.dto.WorkloadRequest;
import com.epam.esm.gym.workload.client.TrainerClient;
import com.epam.esm.gym.workload.dao.InMemoryTrainerDao;
import com.epam.esm.gym.workload.provider.TrainerProfileListProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InMemoryTrainerDaoTest {

    @Mock
    private TrainerClient client;

    @InjectMocks
    private InMemoryTrainerDao inMemoryTrainerDao;

    @BeforeEach
    void setUp() {
        inMemoryTrainerDao = new InMemoryTrainerDao(client);
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerProfileListProvider.class)
    void testFindAll_WithProfiles(List<TrainerProfile> trainerProfiles) {
        inMemoryTrainerDao.save(trainerProfiles);
        List<TrainerProfile> profiles = inMemoryTrainerDao.findAll();
        assertEquals(2, profiles.size());
        assertTrue(profiles.containsAll(trainerProfiles));
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerProfileListProvider.class)
    void testSave_ListOfProfiles(List<TrainerProfile> trainerProfiles) {
        inMemoryTrainerDao.save(trainerProfiles);
        trainerProfiles.forEach(trainerProfile -> {
            TrainerProfile retrieved = inMemoryTrainerDao.findTrainerByUsername(trainerProfile.username());
            assertEquals(trainerProfile, retrieved);
        });
    }

    @Test
    void testFindTrainerByUsernameNotFoundTriggersClientCall() {
        TrainerProfile trainerProfile = TrainerProfile.builder()
                .username("Albus.Dumbledore")
                .firstName("Albus")
                .lastName("Dumbledore")
                .build();

        when(client.findByUsername(trainerProfile.username())).thenReturn(trainerProfile);

        TrainerProfile retrieved = inMemoryTrainerDao.findTrainerByUsername(trainerProfile.username());

        assertNotNull(retrieved);
        assertEquals(trainerProfile, retrieved);
        verify(client, times(1)).findByUsername(trainerProfile.username());
    }

    @Test
    void testFindTrainerByUsernameFoundInMemory() {
        TrainerProfile trainerProfile = TrainerProfile.builder()
                .username("Severus.Snape")
                .firstName("Severus")
                .lastName("Snape")
                .build();

        inMemoryTrainerDao.save(trainerProfile);
        TrainerProfile retrieved = inMemoryTrainerDao.findTrainerByUsername(trainerProfile.username());
        assertEquals(trainerProfile, retrieved);
        verify(client, never()).findByUsername(trainerProfile.username());
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerProfileListProvider.class)
    void testFindAllEmptyProfiles(List<TrainerProfile> trainerProfiles) {
        inMemoryTrainerDao.save(trainerProfiles);
        List<TrainerProfile> profiles = inMemoryTrainerDao.findAll();
        assertEquals(trainerProfiles, profiles);
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerProfileListProvider.class)
    void testFindAllWithProfiles(List<TrainerProfile> trainerProfiles) {
        inMemoryTrainerDao.save(trainerProfiles.get(0));
        inMemoryTrainerDao.save(trainerProfiles.get(1));
        List<TrainerProfile> profiles = inMemoryTrainerDao.findAll();
        assertEquals(2, profiles.size());
        assertTrue(profiles.contains(trainerProfiles.get(0)));
        assertTrue(profiles.contains(trainerProfiles.get(1)));
    }

    @ParameterizedTest
    @ArgumentsSource(TrainerProfileListProvider.class)
    void testSaveListOfProfiles(List<TrainerProfile> trainerProfiles) {
        inMemoryTrainerDao.save(trainerProfiles);
        TrainerProfile retrieved1 = inMemoryTrainerDao.findTrainerByUsername(trainerProfiles.get(0).username());
        TrainerProfile retrieved2 = inMemoryTrainerDao.findTrainerByUsername(trainerProfiles.get(1).username());
        assertEquals(trainerProfiles.get(0), retrieved1);
        assertEquals(trainerProfiles.get(1), retrieved2);
    }

    @Test
    void testGetSummary_NoSummarySaved() {
        assertNull(inMemoryTrainerDao.getSummary());
    }

    @Test
    void testSaveWorkload() {
        WorkloadRequest workloadRequest = new WorkloadRequest(
                "Severus.Snape",
                "Severus",
                "Snape",
                TrainerStatus.ACTIVE,
                LocalDate.now().plusDays(1),
                120,
                ActionType.ADD
        );

        inMemoryTrainerDao.saveWorkload(workloadRequest);
        WorkloadRequest storedRequest = inMemoryTrainerDao.getWorkloadRequest(workloadRequest.trainerUsername());

        assertNotNull(storedRequest);
        assertEquals(workloadRequest, storedRequest);
    }

    @Test
    void testRemoveWorkload() {
        WorkloadRequest workloadRequest = new WorkloadRequest(
                "Severus.Snape",
                "Severus",
                "Snape",
                TrainerStatus.ACTIVE,
                LocalDate.now().plusDays(1),
                120,
                ActionType.ADD
        );

        inMemoryTrainerDao.saveWorkload(workloadRequest);
        inMemoryTrainerDao.removeWorkload(workloadRequest);
        WorkloadRequest removedRequest = inMemoryTrainerDao.getWorkloadRequest(workloadRequest.trainerUsername());
        assertNull(removedRequest);
    }

    @Test
    void testSaveSummary() {
        TrainerRequest trainerRequest = new TrainerRequest();
        trainerRequest.setUsername("Minerva.McGonagall");
        trainerRequest.setFirstName("Minerva");
        trainerRequest.setLastName("McGonagall");
        trainerRequest.setTrainerStatus(TrainerStatus.ACTIVE);

        TrainerSummary trainerSummary = new TrainerSummary(
                trainerRequest, LocalDate.now(), LocalDate.now().plusMonths(1));

        inMemoryTrainerDao.saveSummary(trainerSummary);
        TrainerSummary storedSummary = inMemoryTrainerDao.getSummary();

        assertNotNull(storedSummary);
        assertEquals(trainerSummary, storedSummary);
    }

    @Test
    void testGetSummary_WhenNoSummaryExists() {
        TrainerSummary summary = inMemoryTrainerDao.getSummary();
        assertNull(summary);
    }
}
