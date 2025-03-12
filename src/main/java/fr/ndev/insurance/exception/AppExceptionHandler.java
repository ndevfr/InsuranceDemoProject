package fr.ndev.insurance.exception;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ExceptionResponse handleHttpMessageNotReadableException(
        HttpMessageNotReadableException ex, WebRequest request) {
        List<String> errors = List.of(ex.getMessage());

        return new ExceptionResponse(BAD_REQUEST, errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintValidationException(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        ExceptionResponse exception = new ExceptionResponse(BAD_REQUEST, errors);
        return ResponseEntity.status(BAD_REQUEST).body(exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = ex.getFieldErrors().stream()
                .map(fe -> "%s %s".formatted(fe.getField(), fe.getDefaultMessage()))
                .toList();
        ExceptionResponse exception = new ExceptionResponse(BAD_REQUEST, errors);
        return ResponseEntity.status(ex.getStatusCode()).body(exception);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionResponse> handleResponseException(
            ResponseStatusException ex, WebRequest request) {
        String message = Objects.requireNonNullElse(ex.getReason(), ex.getMessage());
        List <String> errors = List.of(message);
        ExceptionResponse exception = new ExceptionResponse(BAD_REQUEST, errors);
        return ResponseEntity.status(ex.getStatusCode()).body(exception);
    }
}