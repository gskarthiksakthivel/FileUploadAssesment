package com.example.demo.dto;


import lombok.Data;

import java.time.LocalDate;


@Data
public class ExerciseDTO {
    private String code;
    private String source;
    private String codeListCode;
    private String displayValue;
    private String longDescription;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int sortingPriority;
}