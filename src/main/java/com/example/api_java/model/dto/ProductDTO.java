package com.example.api_java.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class ProductDTO {
    private Long productId;
    @DecimalMin(value = "1")
    private BigDecimal productPrice;
    @NotNull
    @NotBlank
    private String productName;
    private String productDescription;
    @NotNull
    private Long cateId;
    private String categoryName;
    private Date productCreateDate;
    @NotNull
    @DecimalMin("0")
    private Integer productRemain;
    private Date productUpDate;
    private Set<Long> featureIds;
    private List<String> imageUrls;
    private Long eventId;


    @JsonIgnore
    private Set<String> featureTypes;
    @JsonIgnore
    private Set<Long> featureTypeId;
    @JsonIgnore
    private List<String> featureSpecific;
    @JsonIgnore
    private List<Long> featurePoint;
    @JsonIgnore
    private String featuresName;


}
