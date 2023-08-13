package com.example.api_java.model.dto;

import com.example.api_java.model.entity.FeatureType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeatureDTO {
    private Long featureFeatureId;
    private Long featureTypeId;
    private String featureSpecific;
    @JsonIgnore
    private FeatureType featureType;
    private Integer featurePoint;
}
