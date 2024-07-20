package com.example.demo.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exercise {
    @Id
    @Column(name="code")
    private String code;
    @Column(name="source")
    private String source;
    @Column(name="codeListCode")
    private String codeListCode;
    @Column(name="displayValue")
    private String displayValue;
    @Column(name="longDescription")
    private String longDescription;
    @Column(name="fromDate")
    private LocalDate fromDate;
    @Column(name="toDate")
    private LocalDate toDate;
    @Column(name="sortingPriority")
    private int sortingPriority;
}
