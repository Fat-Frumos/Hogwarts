package com.epam.esm.gym.user.controller;

import com.epam.esm.gym.jms.dto.MessageResponse;
import com.epam.esm.gym.user.exception.UserAlreadyExistsException;
import com.epam.esm.gym.user.exception.UserNotFoundException;
import com.epam.esm.gym.user.security.service.BruteForceProtectionService;
import com.epam.esm.gym.user.security.service.JwtProvider;
import com.epam.esm.gym.user.security.service.SecurityUserDetailsService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 * This class is responsible for testing the functionality of the ExceptionHandler endpoints.
 *
 * @see GlobalExceptionHandler
 * @see WebMvcTest
 */
@WebMvcTest(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private BruteForceProtectionService bruteForceProtectionService;
    @MockBean
    private SecurityUserDetailsService userDetailsService;

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");
        ResponseEntity<MessageResponse> response = globalExceptionHandler.handleIllegalArgumentException(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Objects.requireNonNull(response.getBody()).message()).isEqualTo("Invalid argument");
    }

    @Test
    void handleUserAlreadyExistsException() {
        UserAlreadyExistsException ex = new UserAlreadyExistsException("User already exists");
        ResponseEntity<MessageResponse> response = globalExceptionHandler.handleUserAlreadyExistsException(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(Objects.requireNonNull(response.getBody()).message()).isEqualTo("User already exists");
    }

    @Test
    void handleMissingParams() {
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("param", "String");
        ResponseEntity<MessageResponse> response = globalExceptionHandler.handleMissingParams(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().message()).isEqualTo("Required request parameter 'param' is not present");
    }

    @Test
    void handleMethodNotSupported() {
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("POST");
        ResponseEntity<MessageResponse> response = globalExceptionHandler.handleMethodNotSupported(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
        assertThat(Objects.requireNonNull(response.getBody()).message())
                .isEqualTo("Request method 'POST' not supported");
    }

    @Test
    void handleValidationExceptions() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "field", "Validation failed");
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));
        ResponseEntity<MessageResponse> response = globalExceptionHandler.handleValidationExceptions(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Objects.requireNonNull(response.getBody()).message()).isEqualTo("field: Validation failed");
    }

    @Test
    void handleException() {
        Exception ex = new Exception("Unexpected error");
        ResponseEntity<MessageResponse> response = globalExceptionHandler.handleException(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(Objects.requireNonNull(response.getBody()).message())
                .isEqualTo("An unexpected error occurred ");
    }

    @Test
    void handleRuntimeException() {
        RuntimeException ex = new RuntimeException("Runtime error");
        ResponseEntity<MessageResponse> response = globalExceptionHandler.handleRuntimeException(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(Objects.requireNonNull(response.getBody()).message()).isEqualTo("An unexpected error occurred ");
    }

    @Test
    void handleMissingRequestBody() {
        HttpInputMessage inputMessage = new HttpInputMessage() {
            @Override
            public InputStream getBody() {
                return new ByteArrayInputStream(new byte[0]);
            }

            @Override
            public HttpHeaders getHeaders() {
                return new HttpHeaders();
            }
        };

        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Missing request body", inputMessage);
        ResponseEntity<MessageResponse> response = globalExceptionHandler.handleMissingRequestBody(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Objects.requireNonNull(response.getBody()).message())
                .isEqualTo("Request body is missing or invalid.");
    }

    @Test
    void handleEntityNotFoundException() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");
        ResponseEntity<MessageResponse> response = globalExceptionHandler.handleEntityNotFoundException(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(Objects.requireNonNull(response.getBody()).message()).isEqualTo("Entity not found");
    }

    @Test
    void handleUserNotFoundException() {
        UserNotFoundException ex = new UserNotFoundException("User not found");
        ResponseEntity<MessageResponse> response = globalExceptionHandler.handleUserNotFoundException(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(Objects.requireNonNull(response.getBody()).message()).isEqualTo("User not found");
    }

    @Test
    void testHandleValidationExceptions() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Constraint violation");
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);
        ConstraintViolationException ex = new ConstraintViolationException("Constraint violation", violations);
        ResponseEntity<MessageResponse> response = globalExceptionHandler.handleValidationExceptions(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Objects.requireNonNull(response.getBody()).message()).isEqualTo("Constraint violation");
    }


    @Test
    void handleSignatureException() {
        RuntimeException ex = new RuntimeException("Invalid signature");
        ResponseEntity<MessageResponse> response = globalExceptionHandler.handleSignatureException(ex);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(Objects.requireNonNull(response.getBody()).message()).isEqualTo("Invalid signature");
    }
}
