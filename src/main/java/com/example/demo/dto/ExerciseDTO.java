package com.example.demo.dto;



import java.time.LocalDate;


public record ExerciseDTO(
        String code,
        String source,
        String codeListCode,
        String displayValue,
        String longDescription,
        LocalDate fromDate,
        LocalDate toDate,
        int sortingPriority
) {}