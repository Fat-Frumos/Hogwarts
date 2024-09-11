package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.auth.BaseResponse;
import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingResponse;
import com.epam.esm.gym.dto.training.TrainingTypeDto;
import com.epam.esm.gym.service.TrainingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Controller for managing training-related operations.
 *
 * <p>This REST controller handles HTTP requests related to training sessions and training types.
 * It implements the endpoints defined in the {@link ITrainingController} interface.</p>
 *
 * <p>The class is annotated with {@link RestController} to designate it as a Spring MVC controller,
 * where methods return domain objects directly rather than views. The {@link Validated} annotation
 * ensures that input parameters and request bodies are validated according to constraints. The
 * {@link AllArgsConstructor} annotation generates a constructor with parameters for all fields,
 * which facilitates dependency injection for any required services or components.</p>
 *
 * <p>Endpoint mappings are defined in the {@link ITrainingController} interface and managed
 * by this controller to provide functionality for retrieving training types and adding new training sessions.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/trainings")
public class TrainingController implements ITrainingController {

    private TrainingService trainingService;

    /**
     * {@inheritDoc}
     * Retrieves a list of all available training types.
     *
     * @return A {@link ResponseEntity} containing a list of {@link com.epam.esm.gym.dto.training.TrainingTypeDto}.
     * Returns HTTP 200 if the list is successfully retrieved.
     * Includes HTTP 401 if unauthorized and HTTP 403 if access is forbidden.
     */
    @Override
    @GetMapping("/types")
    @PreAuthorize("hasAuthority('ROLE_TRAINER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<TrainingTypeDto>> getTrainingTypes() {
        List<TrainingTypeDto> types = trainingService.getTrainingTypes();
        return ResponseEntity.ok(types);
    }

    /**
     * Retrieves a list of trainings accessible by users with roles of 'TRAINER', 'TRAINEE', or 'ADMIN'.
     * This method delegates the request to the {@link TrainingService} to fetch the list of trainings and
     * returns them as a response. Access is controlled through Spring Security annotations to ensure that
     * only users with the appropriate roles can access this endpoint.
     *
     * @return a {@link ResponseEntity} containing a list of {@link TrainingResponse} objects with status OK.
     */
    @Override
    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_TRAINER') or hasAuthority('ROLE_TRAINEE') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<TrainingResponse>> getTrainings() {
        return trainingService.getAllTrainings();
    }

    /**
     * {@inheritDoc}
     * Creates a new training session.
     *
     * @param request The {@link TrainingRequest} object containing details of the training session to be created.
     * @return A {@link ResponseEntity} with HTTP 201 if the training session is successfully created.
     * Includes HTTP 400 for bad request HTTP 401 if unauthorized, and HTTP 403 if access is forbidden.
     */
    @Override
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_TRAINER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse> addTraining(
            @Valid @RequestBody TrainingRequest request) {
        return trainingService.createTraining(request);
    }
}
