package com.epam.esm.gym.service;

import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.dto.profile.ProfileResponse;
import com.epam.esm.gym.dto.trainee.TraineeProfile;
import com.epam.esm.gym.dto.trainee.TraineeRequest;
import com.epam.esm.gym.dto.trainer.SlimTrainerProfile;
import com.epam.esm.gym.dto.trainer.TrainerProfile;
import com.epam.esm.gym.dto.training.TrainingResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing trainee profiles and related operations.
 * <p>
 * This interface defines the contract for various operations related to trainees, including registration, profile
 * retrieval, updating, and managing assigned trainers. It also provides methods for validating user requests, changing
 * passwords, and handling training sessions associated with trainees. Each method returns a `ResponseEntity` to handle
 * HTTP responses and status codes appropriately.
 * </p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public interface TraineeService {

    /**
     * Registers a new trainee with the given details and returns a profile response.
     * <p>
     * This method processes the registration request for a new trainee by creating a new trainee profile from the
     * provided `TraineeRequest` object. Upon successful registration, it returns a `ResponseEntity` containing the
     * newly created `ProfileResponse`. If the registration fails, an appropriate HTTP status code will be returned
     * indicating the failure reason. This method ensures that the trainee's information
     * is properly validated and saved before returning the response.
     * </p>
     *
     * @param request the details of the trainee to be registered
     * @return a {@link ResponseEntity} containing the {@link ProfileResponse} of the newly registered trainee
     */
    ResponseEntity<ProfileResponse> register(TraineeRequest request);

    /**
     * Deletes a trainee profile identified by the provided username.
     * <p>
     * This method removes the profile of the trainee whose username is given in the `username` parameter. If the
     * deletion is successful, it returns a `ResponseEntity` with no content and an HTTP status of `204 No Content`.
     * If the trainee is not found or an error occurs during deletion, an appropriate HTTP status code is returned to
     * indicate the problem. This method ensures that the trainee is no longer present in the system after successful
     * deletion.
     * </p>
     *
     * @param username the username of the trainee to be deleted
     * @return a {@link ResponseEntity} with no content if the deletion is successful
     */
    ResponseEntity<Void> deleteTrainee(String username);

    /**
     * Retrieves the profile of a trainee by their username.
     * <p>
     * This method fetches the profile details of a trainee specified by the provided username. It returns a
     * `ResponseEntity` containing the `TraineeProfile` of the requested trainee if found.
     * If the trainee does not exist, an appropriate HTTP status code will be returned to indicate the absence.
     * This method is useful for fetching and displaying trainee information based on their username.
     * </p>
     *
     * @param username the username of the trainee whose profile is to be retrieved
     * @return a {@link ResponseEntity} containing the {@link TraineeProfile} of the requested trainee
     */
    ResponseEntity<TraineeProfile> getTraineeProfileByName(String username);

    /**
     * Updates an existing trainee profile with the new details provided.
     * <p>
     * This method updates the profile of the trainee identified by the username with the information contained in the
     * `TraineeRequest`. If the update is successful, it returns a `ResponseEntity` with the updated `TraineeProfile`.
     * If the trainee is not found or the update fails, an appropriate HTTP status code is returned.
     * This method ensures that the trainee's profile is modified according to the provided request details.
     * </p>
     *
     * @param username the username of the trainee whose profile is to be updated
     * @param request  the updated details of the trainee
     * @return a {@link ResponseEntity} containing the updated {@link TraineeProfile}
     */
    ResponseEntity<TraineeProfile> updateTrainee(
            String username, TraineeRequest request);

    /**
     * Validates the provided user profile request for correctness and completeness.
     * <p>
     * This method checks whether the profile request adheres to required constraints and validation rules. It ensures
     * that all necessary fields are present and meet the specified criteria. The method returns `true` if the request
     * is valid and `false` otherwise. This is crucial for maintaining data integrity and ensuring that only valid data
     * is processed by the system.
     * </p>
     *
     * @param request the profile request to be validated
     * @return true if the profile request is valid, false otherwise
     */
    boolean validateUser(ProfileRequest request);

    /**
     * Changes the password of a user based on the provided profile request.
     * <p>
     * This method updates the password of the user specified in the `ProfileRequest`. It validates the new password
     * and ensures that the update is applied correctly. If the password change is successful, the method will complete
     * without returning any content. If an error occurs during the update, an appropriate HTTP status code will be
     * returned to indicate the failure.
     * </p>
     *
     * @param request the profile request containing the new password
     */
    void changePassword(ProfileRequest request);

    /**
     * Updates the list of trainers assigned to a specific trainee.
     * <p>
     * This method modifies the list of trainers assigned to the trainee identified by the given username.
     * The list of trainers is updated based on the provided usernames. If the update is successful, the response
     * contains the updated list of trainer profiles. If the trainee is not found or the update fails,
     * an appropriate HTTP status code will be returned.
     * </p>
     *
     * @param username          the username of the trainee whose trainers are to be updated
     * @param trainersUsernames the list of usernames of the trainers to be assigned
     * @return a {@link ResponseEntity} containing the updated list of {@link TrainerProfile}
     */
    ResponseEntity<List<SlimTrainerProfile>> updateTraineeTrainersByName(
            String username, List<String> trainersUsernames);

    /**
     * Retrieves a list of trainings associated with a specific trainee.
     * <p>
     * This method returns the list of training responses for the trainee identified by the provided username. The list
     * can be filtered based on additional query parameters. The response contains the list of training responses if
     * the retrieval is successful. If no trainings are found or an error occurs, an appropriate HTTP status code is
     * returned.
     * </p>
     *
     * @param username the username of the trainee whose trainings are to be retrieved
     * @param params   the query parameters to filter the trainings
     * @return a {@link ResponseEntity} containing the list of {@link TrainingResponse}
     */
    ResponseEntity<List<TrainingResponse>> getTraineeTrainingsByName(
            String username, Map<String, String> params);

    /**
     * Activates or deactivates the profile of a trainee.
     * <p>
     * This method changes the active status of the trainee's profile based on the provided username and active flag.
     * If the operation is successful, the method returns a `ResponseEntity` with no content. If the trainee is not
     * found or the operation fails, an appropriate HTTP status code is returned. This method is useful for managing
     * the trainee's profile status.
     * </p>
     *
     * @param username the username of the trainee whose profile is to be activated or deactivated
     * @param active   the desired active status of the profile
     * @return a {@link ResponseEntity} with no content if the operation is successful
     */
    ResponseEntity<Void> activateDeactivateProfile(String username, Boolean active);

    /**
     * Retrieves a list of trainers who are not currently assigned to a specific trainee.
     * <p>
     * This method returns a list of trainers who are not assigned to the trainee identified by the given username.
     * The response contains the list of unassigned trainer profiles. If the retrieval is successful,
     * the list will be included in the response entity. If no trainers are found or an error occurs,
     * an appropriate HTTP status code will be returned.
     * </p>
     *
     * @param username the username of the trainee for whom the unassigned trainers are to be retrieved
     * @return a {@link ResponseEntity} containing the list of {@link TrainerProfile}
     */
    ResponseEntity<List<SlimTrainerProfile>> getNotAssignedTrainers(String username);


    /**
     * Retrieves a trainee by their username.
     * <p>
     * This method returns the trainee object identified by the provided username. It retrieves the complete details of
     * the trainee if found. If the trainee does not exist, an appropriate HTTP status code will be returned to indicate
     * the absence. This method is useful for accessing the trainee's full record based on their username.
     * </p>
     *
     * @param traineeUsername the username of the trainee to be retrieved
     * @return the {@link Trainee} object if found
     */
    Trainee getTrainee(String traineeUsername);

    /**
     * Retrieves a list of all trainees.
     * <p>
     * This method returns a list of `TraineeProfile` objects representing all the trainees in the system.
     * The response entity contains the list of profiles if the retrieval is successful.
     * If no trainees are found or an error occurs, an appropriate HTTP status code will be returned.
     * This method is useful for obtaining a comprehensive list of all trainees.
     * </p>
     *
     * @return a {@link ResponseEntity} containing the list of {@link TraineeProfile}
     */
    ResponseEntity<List<TraineeProfile>> findAll();
}
