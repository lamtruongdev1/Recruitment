package com.poly.Recruitment.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CvDto {
    private Long id;
    private String name;
    private String description;
    private String fileUrl; // URL for the file
    private BigDecimal price;
    // Constructors, getters, and setters
}