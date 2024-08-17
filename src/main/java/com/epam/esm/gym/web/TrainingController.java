package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.training.TrainingRequest;
import com.epam.esm.gym.dto.training.TrainingTypeResponse;
import com.epam.esm.gym.service.TrainingService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trainings")
@AllArgsConstructor
public class TrainingController {

    private TrainingService trainingService;

    @GetMapping("/types")
    public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes() {
        List<TrainingTypeResponse> types = trainingService.getTrainingTypes();
        return ResponseEntity.ok(types);
    }

    @PostMapping("/")
    public ResponseEntity<Void> addTraining(@RequestBody TrainingRequest request) {
        trainingService.createTraining(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
