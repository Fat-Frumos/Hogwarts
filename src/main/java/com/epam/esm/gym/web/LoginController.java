package com.epam.esm.gym.web;

import com.epam.esm.gym.dto.profile.MessageResponse;
import com.epam.esm.gym.dto.profile.ProfileRequest;
import com.epam.esm.gym.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor

public class LoginController implements ILoginController {

    private final UserService userService;

    @Override
    public ResponseEntity<MessageResponse> login(
            @RequestParam("username") @NotNull @Valid @Size(min = 2, max = 50) String username,
            @RequestParam("password") @NotNull @Valid @Size(min = 6, max = 50) String password) {
        return userService.authenticate(username, password);
    }

    @Override
    public ResponseEntity<MessageResponse> changeLogin(
            @RequestBody @Valid ProfileRequest request) {
        return userService.changePassword(request);
    }
}
