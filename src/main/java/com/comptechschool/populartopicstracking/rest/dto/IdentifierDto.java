package com.comptechschool.populartopicstracking.rest.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentifierDto {

    @ApiModelProperty(example = "564245215", notes = "Entity id", position = 0)
    private Long id;

    @ApiModelProperty(example = "like", notes = "Action type of this identifier", position = 1)
    private String action;

    @ApiModelProperty(example = "94", notes = "Frequency of occurrence of this identifier", position = 2)
    private Long frequency;
}
