package com.example.aquariuxtechnical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    private String message;

    private String details;

    private LocalDateTime timestamp;

    private HttpStatus status;

    public static ErrorResponseDTO create(String message, String details, HttpStatus status) {
        return new ErrorResponseDTO(message, details, LocalDateTime.now(), status);
    }
}
