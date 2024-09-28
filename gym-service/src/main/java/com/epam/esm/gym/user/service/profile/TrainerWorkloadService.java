package com.epam.esm.gym.user.service.profile;

import com.epam.esm.gym.jms.dto.ActionType;
import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.jms.dto.WorkloadRequest;
import com.epam.esm.gym.user.broker.TrainerProducer;
import com.epam.esm.gym.user.dao.JpaTrainerDao;
import com.epam.esm.gym.user.dao.JpaTrainingDao;
import com.epam.esm.gym.user.entity.Trainer;
import com.epam.esm.gym.user.entity.Training;
import com.epam.esm.gym.user.exception.UserNotFoundException;
import com.epam.esm.gym.user.mapper.TrainingMapper;
import com.epam.esm.gym.user.service.WorkloadService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.epam.esm.gym.jms.ActiveMQConfig.TRAINER_QUEUE;

/**
 * Service class that manages operations related to trainer workloads.
 * It retrieves and updates workload information for trainers.
 */
@Service
@AllArgsConstructor
public class TrainerWorkloadService implements WorkloadService {

    private final TrainerProducer producer;
    private final JpaTrainerDao trainerRepository;
    private final JpaTrainingDao trainingRepository;

    /**
     * Updates the workload based on the provided request.
     * This method publishes the workload update to a messaging service.
     *
     * @param request the workload request containing the details to be updated
     * @return MessageResponse indicating the result of the update operation
     */
    @Override
    public MessageResponse updateWorkload(WorkloadRequest request) {
        ActionType.fromString(request.actionType());

        Trainer trainer = trainerRepository.findByUsername(request.trainerUsername())
                .orElseThrow(() -> new UserNotFoundException("Trainer not found for username: "
                        + request.trainerUsername()));

        int trainingDuration = request.trainingDuration();
        List<Training> trainings = trainingRepository
                .findByTrainerAndDate(trainer.getUsername(), request.trainingDate());
        if (trainings.isEmpty()) {
            throw new EntityNotFoundException(String.format("Data %s or trainer %s not found",
                    request.trainingDate(), request.trainerUsername()));
        }

        Training training = trainings.getFirst();
        if (request.actionType() == ActionType.ADD) {
            training.setTrainingDuration(training.getTrainingDuration() + trainingDuration);
            trainingRepository.save(training);
        } else if (request.actionType() == ActionType.DELETE) {
            int duration = training.getTrainingDuration() - trainingDuration;
            if (duration < 0) {
                trainingRepository.delete(training);
                producer.publishWorkloadResponse(request);
                return new MessageResponse("Training was deleted successfully: " + request);
            } else {
                training.setTrainingDuration(duration);
                trainingRepository.save(training);
            }
        }
        producer.publishWorkloadResponse(request);
        producer.publishTrainingResponse(TRAINER_QUEUE, TrainingMapper.toDto(training));
        return new MessageResponse("Workload was updated successfully: " + request);
    }
}
