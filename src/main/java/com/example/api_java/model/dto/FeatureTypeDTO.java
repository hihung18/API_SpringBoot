package com.example.api_java.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
public class FeatureTypeDTO {
    private Long featureTypeId;
    @NotNull
    private String featureTypeName;
    private String featureTypeUnit;
}
