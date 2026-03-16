package com.carbonaze.backend.exception;

import com.carbonaze.backend.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException exception,
                                                                HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException exception,
                                                               HttpServletRequest request) {
        String message = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getField)
            .distinct()
            .collect(Collectors.joining(", "));

        return buildResponse(HttpStatus.BAD_REQUEST,
            "Champs invalides ou manquants: " + message,
            request.getRequestURI());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException exception,
                                                                   HttpServletRequest request) {
        String message = exception.getConstraintViolations()
            .stream()
            .map(constraintViolation -> {
                String path = constraintViolation.getPropertyPath().toString();
                int lastSeparatorIndex = path.lastIndexOf('.');
                return lastSeparatorIndex >= 0 ? path.substring(lastSeparatorIndex + 1) : path;
            })
            .distinct()
            .collect(Collectors.joining(", "));

        return buildResponse(HttpStatus.BAD_REQUEST,
            "Champs invalides ou manquants: " + message,
            request.getRequestURI());
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, String path) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(status.value());
        errorResponse.setError(status.getReasonPhrase());
        errorResponse.setMessage(message);
        errorResponse.setPath(path);
        return ResponseEntity.status(status).body(errorResponse);
    }
}
