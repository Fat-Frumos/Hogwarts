package com.epam.esm.gym.service;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.Trainer;
import com.epam.esm.gym.dto.trainer.TrainerResponseDto;
import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerRequest;
import com.epam.esm.gym.dto.trainer.PutTrainerRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * Provides operations for managing trainer profiles and related tasks.
 * <p>
 * This service includes methods for changing trainer passwords, deleting trainer accounts,
 * and registering new trainers. It also handles retrieving trainer profiles, updating trainer details,
 * and listing trainers who are not assigned to any trainees.  Additionally, it provides functionality
 * for activating or deactivating trainer profiles and assigning trainees to trainers.
 * Each method returns appropriate responses based on the operation's success or failure.
 * </p>
 */
public interface TrainerService {

    /**
     * Changes the password for a trainer based on the provided profile request.
     * <p>
     * This method updates the password for the trainer identified by the username in the `ProfileRequest`.
     * It ensures that the trainer's password is changed securely and efficiently.
     * The method does not return a value but performs the update operation.
     * Password changes are crucial for maintaining account security and integrity.
     * </p>
     *
     * @param request the profile request containing the new password details
     */
    void changeTrainerPassword(ProfileRequest request);

    /**
     * Deletes a trainer's profile using their username.
     * <p>
     * This method removes the trainer with the specified username from the system.
     * The deletion process ensures that all associated data is also removed to maintain data consistency.
     * If the trainer does not exist, the method handles this appropriately.
     * The method does not return a value but ensures the trainer is completely removed.
     * </p>
     *
     * @param username the username of the trainer to be deleted
     */
    ResponseEntity<BaseResponse> deleteTrainer(String username);

    /**
     * Registers a new trainer using the provided registration details.
     * <p>
     * This method processes the `TrainerRequest` to create a new trainer profile.
     * Upon successful registration, it returns a `ResponseEntity` containing a `ProfileResponse`
     * with the trainer's details. If the registration fails, the method returns an appropriate HTTP status code.
     * The registration process includes validation of the provided details and ensures that the trainer is added.
     * </p>
     *
     * @param request the registration details for the new trainer
     * @return a {@link ResponseEntity} containing the {@link ProfileResponse} of the newly registered trainer
     */
    ResponseEntity<ProfileResponse> registerTrainer(TrainerRequest request);

    /**
     * Retrieves the profile of a trainer identified by their username.
     * <p>
     * This method fetches the `TrainerProfile` for the trainer specified by the given username.
     * If the trainer exists, the profile details are returned in a `ResponseEntity`.
     * If the trainer is not found, an appropriate HTTP status code is returned to indicate the absence.
     * Retrieving trainer profiles helps in viewing and managing trainer information.
     * </p>
     *
     * @param username the username of the trainer whose profile is to be retrieved
     * @return a {@link ResponseEntity} containing the
     * {@link com.epam.esm.gym.dto.trainer.TrainerProfile} of the requested trainer
     */
    ResponseEntity<BaseResponse> getTrainerProfileByName(String username);

    /**
     * Updates the profile details of an existing trainer.
     * <p>
     * This method updates the trainer profile with the information provided in the `TrainerUpdateRequest`.
     * Upon successful update, it returns a `ResponseEntity` containing the updated `TrainerProfile`.
     * The update process includes validation of the new details and ensures that the trainer's profile
     * is correctly updated. If the update fails, the method returns an appropriate HTTP status code.
     * </p>
     *
     * @param username the username of the trainer to be updated
     * @param request  the details to update the trainer's profile
     * @return a {@link ResponseEntity} containing the updated {@link com.epam.esm.gym.dto.trainer.TrainerProfile}
     */
    ResponseEntity<BaseResponse> updateTrainer(String username, PutTrainerRequest request);

    /**
     * Retrieves trainers who are not assigned to the specified trainee.
     * <p>
     * This method returns a list of `TrainerProfile` objects for trainers who are not currently assigned
     * to the trainee with the specified username.
     * The result is returned in a `ResponseEntity` and includes an appropriate HTTP status code.
     * This functionality helps in finding available trainers for assignment to new trainees.
     * </p>
     *
     * @param traineeUsername the username of the trainee for whom to find not assigned trainers
     * @return a {@link ResponseEntity} containing a list of
     * {@link com.epam.esm.gym.dto.trainer.TrainerProfile} for not assigned trainers
     */
    ResponseEntity<List<TrainerProfile>> getNotAssigned(String traineeUsername);

    /**
     * Activates or deactivates a trainer's profile.
     * <p>
     * Method updates the active status of the trainer identified by the username based on the provided boolean value.
     * If the operation is successful, it returns a `ResponseEntity`
     * with no content and an HTTP status code of `204 No Content`.
     * The method ensures that the trainer's profile is either activated or deactivated as required.
     * This functionality is crucial for managing the availability of trainers in the system.
     * </p>
     *
     * @param username the username of the trainer whose profile status is to be updated
     * @param active   boolean indicating whether to activate or deactivate the profile
     * @return a {@link ResponseEntity} with no content if the operation is successful
     */
    ResponseEntity<Void> activateDeactivateProfile(String username, Boolean active);

    /**
     * Retrieves a trainer by their username.
     * <p>
     * This method fetches the `Trainer` object associated with the given username. It provides the trainer's detailed
     * information, which can be used for further processing or operations. If the trainer does not exist,
     * it may result in an exception or error based on implementation.
     * </p>
     *
     * @param trainerUsername the username of the trainer to retrieve
     * @return the {@link Trainer} object associated with the provided username
     */
    Optional<Trainer> getTrainer(String trainerUsername);

    /**
     * Retrieves a list of all trainers in the system.
     * <p>
     * This method returns a `ResponseEntity` containing a list of `TrainerProfile` objects representing all trainers.
     * This can be used to display or process profiles of all registered trainers.
     * The result includes all trainers without any filtering or pagination.
     * </p>
     *
     * @return a {@link ResponseEntity} containing a list of
     * {@link com.epam.esm.gym.dto.trainer.TrainerProfile} for all trainers
     */
    ResponseEntity<List<TrainerProfile>> findAll();

    /**
     * Assigns a trainee to a trainer.
     * <p>
     * This method handles the assignment of a trainee to a trainer based on the provided trainee username.
     * It updates relevant records to establish the trainee-trainer relationship. The method does not return a value
     * but ensures the assignment is processed correctly.
     * </p>
     *
     * @param traineeUsername the username of the trainee to be assigned to a trainer
     */
    void assignTraineeToTrainer(String traineeUsername);

    /**
     * Retrieves a list of trainers who are not assigned to the specified trainee and are active.
     *
     * @param trainee the trainee whose unassigned active trainers are to be retrieved.
     *                Must not be {@code null}. The username of the trainee is used to
     *                find trainers who are not assigned to this trainee.
     * @return a {@link ResponseEntity} containing a list of {@link TrainerResponseDto} objects
     * representing the active trainers who are not assigned to the trainee.
     * Each {@link TrainerResponseDto} includes the trainer's username, first name,
     * last name, and specialization.
     * The response will have an HTTP status code of 200 (OK) if the request is successful.
     * If no trainers are found, the response will still be 200 but with an empty list.
     */
    ResponseEntity<List<TrainerResponseDto>> getActiveTrainersForTrainee(Trainee trainee);
}
