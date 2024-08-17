package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.TrainingTypeResponseDto;
import com.epam.esm.gym.service.TrainingService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trainings")
@AllArgsConstructor
public class TrainingController {

    private TrainingService trainingService;

    @GetMapping("/types")
    public ResponseEntity<List<TrainingTypeResponseDto>> getTrainingTypes() {
        List<TrainingTypeResponseDto> types = trainingService.getTrainingTypes();
        return ResponseEntity.ok(types);
    }
}
