package com.epam.esm.gym.user.service;

import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.dto.training.TrainingProfile;
import com.epam.esm.gym.user.dto.training.TrainingRequest;
import com.epam.esm.gym.jms.dto.TrainingResponse;
import com.epam.esm.gym.user.dto.training.TrainingTypeDto;
import com.epam.esm.gym.user.entity.TrainingType;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Service interface for managing training sessions.
 * This interface defines methods for interacting with training sessions, including retrieving available training types,
 * fetching trainings conducted by a specific trainer, and creating new training sessions. The methods allow for
 * querying and manipulating training data within the system, providing functionality for both administrative and user
 * interaction purposes.
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public interface TrainingService {

    /**
     * Retrieves all available training types.
     * <p>
     * This method queries the system to retrieve a comprehensive list of all training types currently available.
     * Each type is represented as a {@link com.epam.esm.gym.user.dto.training.TrainingTypeDto} object, which includes
     * relevant details about the training type. This information is useful for displaying a list of options to users,
     * such as in dropdown menus. The list returned may be used to filter or categorize training sessions.
     * </p>
     *
     * @return a list of {@link com.epam.esm.gym.user.dto.training.TrainingTypeDto}
     * representing all available training types
     */
    List<TrainingTypeDto> getTrainingTypes();

    /**
     * Retrieves a list of trainings associated with a specific trainer.
     * <p>
     * This method returns a {@link ResponseEntity} that contains a list of {@link TrainingResponse} objects, which
     * represent the trainings conducted by the trainer identified by the username. The results can be filtered
     * based on the criteria specified in the {@link TrainingProfile} object. This is useful for retrieving and
     * displaying the trainings associated with a  trainer, along with details like training dates and durations.
     * </p>
     *
     * @param request the {@link TrainingProfile} criteria for filtering the trainings, such as date ranges or types
     * @return a {@link ResponseEntity} containing a list of {@link TrainingResponse}
     */
    List<TrainingResponse> getTrainerTrainingsByName(TrainingProfile request);

    /**
     * Creates a new training session in the system.
     * <p>
     * This method processes a {@link TrainingRequest} to create a new training session with the specified details. The
     * method involves validating the request data, saving the new training session to the database, and potentially
     * performing additional operations such as notifying involved parties or updating related records. There is no
     * direct return value from this method, but the new training session will be persisted, and any errors encountered
     * during processing will be handled according to the application's error handling mechanisms.
     * </p>
     *
     * @param request the {@link TrainingRequest} containing the details for the new training session
     */
    TrainingResponse createTraining(TrainingRequest request);

    /**
     * Retrieves a list of all training sessions.
     * This method fetches all available training sessions from the system and returns them as a list
     * of {@link TrainingResponse} objects. The list is encapsulated in a {@link ResponseEntity}
     * which allows for additional HTTP response details to be included.
     *
     * @return a {@link ResponseEntity} containing a list of {@link TrainingResponse} objects.
     * The HTTP status code is typically {@code 200 OK} if the request is successful.
     */
    List<TrainingResponse> getAllTrainings();

    /**
     * Saves the given TrainingType entity to the database.
     *
     * @param type the TrainingType entity to be saved
     * @return the saved TrainingType entity, including any generated fields
     */
    TrainingType save(TrainingType type);

    /**
     * Deletes a training session profile by username  to the database.
     *
     * @param trainingName The username of the trainer to be deleted.
     */
    MessageResponse removeTraining(String trainingName);
}
