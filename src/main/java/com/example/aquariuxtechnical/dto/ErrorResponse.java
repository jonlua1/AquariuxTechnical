package com.example.aquariuxtechnical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;

    private String details;

    private LocalDateTime timestamp;

    private HttpStatus status;

    public static ErrorResponse create(String message, String details, HttpStatus status) {
        return new ErrorResponse(message, details, LocalDateTime.now(), status);
    }
}
