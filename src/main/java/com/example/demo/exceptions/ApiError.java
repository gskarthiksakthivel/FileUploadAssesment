package com.example.demo.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
@Data
public class ApiError {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private List<String> messages;
}
