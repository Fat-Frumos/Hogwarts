package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    @Operation(summary = "3. Login a user")
    public ResponseEntity<MessageResponse> login(
            @RequestParam("username") @NotNull @Valid @Size(min = 2, max = 50) String username,
            @RequestParam("password") @NotNull @Valid @Size(min = 6, max = 50) String password) {
        return userService.authenticate(username, password);
    }

    @PutMapping("/login/change")
    @Operation(summary = "4. Change Login")
    public ResponseEntity<MessageResponse> changeLogin(
            @RequestBody @Valid ProfileRequest request) {
        return userService.changePassword(request);
    }
}
