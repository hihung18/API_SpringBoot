package com.example.api_java.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;


@Getter
@Setter
public class ImageDTO {
    private Long imageId;
    private String imageUrl;
    private String imageAlt;
    @DecimalMin(value = "0")
    private Integer imageWidth;
    @DecimalMin(value = "0")
    private Integer imageHeight;
    private Long productId;
}
