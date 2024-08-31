package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingTypeResponse;
import com.epam.esm.gym.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/trainings")
@AllArgsConstructor
public class TrainingController {

    private TrainingService trainingService;

    @GetMapping("/types")
    @Operation(summary = "17. Get Training Types")
    @PreAuthorize("hasAuthority('ROLE_TRAINER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes() {
        List<TrainingTypeResponse> types = trainingService.getTrainingTypes();
        return ResponseEntity.ok(types);
    }

    @PostMapping("/add")
    @Operation(summary = "14. Add Training")
    @PreAuthorize("hasAuthority('ROLE_TRAINER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> addTraining(
            @Valid @RequestBody TrainingRequest request) {
        trainingService.createTraining(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
