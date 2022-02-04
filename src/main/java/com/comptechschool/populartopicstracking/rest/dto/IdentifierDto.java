package com.comptechschool.populartopicstracking.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentifierDto {
    private Long id;
    private String action;
    private Long frequency;
}