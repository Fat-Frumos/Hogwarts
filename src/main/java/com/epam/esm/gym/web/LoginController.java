package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    @Operation(summary = "3. Login a user")
    public ResponseEntity<Void> login(
            @RequestBody ProfileRequest request) {
        userService.authenticate(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/login/change")
    @Operation(summary = "4. Change Login")
    public ResponseEntity<Void> changeLogin(
            @RequestBody ProfileRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok().build();
    }
}
