package com.epam.esm.gym.workload.broker;

import com.epam.esm.gym.jms.dto.TrainerProfile;
import com.epam.esm.gym.workload.dao.InMemoryDao;
import com.epam.esm.gym.workload.service.TrainerReceiverService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Integration tests for the WarehouseReceiveServiceTest class.
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
class TrainerReceiverServiceTest {

    @MockBean
    private InMemoryDao holderTrainer;

    @MockBean
    private JmsTemplate jmsTemplate;

    @Autowired
    private TrainerReceiverService trainerReceiverService;

    @Test
    void testReceiveTrainerProfile() {
        TrainerProfile profile = new TrainerProfile("harry_potter", "Harry", "Potter", true, new ArrayList<>());
        trainerReceiverService.receive(profile.toString());
    }

    @Test
    void testReceiveTrainerProfileList() {
        List<TrainerProfile> profiles = List.of(
                new TrainerProfile("harry_potter", "Harry", "Potter", true, new ArrayList<>()),
                new TrainerProfile("hermione_granger", "Hermione", "Granger", true, new ArrayList<>())
        );

        trainerReceiverService.receiveTrainerProfileList(profiles);

        verify(holderTrainer, times(1)).save(eq(profiles));
    }

    @Test
    void testReceiveTrainerProfile_invalidProfile() {
        trainerReceiverService.receive("");
    }

    @Test
    void testReceiveTrainerProfileList_emptyList() {
        trainerReceiverService.receiveTrainerProfileList(Collections.emptyList());
    }
}
