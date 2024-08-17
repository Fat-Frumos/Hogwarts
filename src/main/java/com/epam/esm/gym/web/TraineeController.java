package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.ResponseRegistrationDto;
import com.epam.esm.gym.dto.TraineeRequestRegistrationDto;
import com.epam.esm.gym.service.TraineeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@AllArgsConstructor
@RequestMapping("/api/trainees")
@Tag(name = "Trainee", description = "Trainee related operations")
public class TraineeController {

    private TraineeService traineeService;


    @PostMapping("/register")
    @Operation(summary = "Register a new trainee")
    public ResponseEntity<ResponseRegistrationDto> registerTrainee(
            @RequestBody TraineeRequestRegistrationDto request) {
        ResponseRegistrationDto response = traineeService.registerTrainee(request);
        return ResponseEntity.ok(response);
    }
}
