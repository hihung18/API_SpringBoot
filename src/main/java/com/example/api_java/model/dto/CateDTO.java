package com.example.api_java.model.dto;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Schema(name = "CateDTO")
public class CateDTO {
    private Long cateId;
    @NotNull
    private String categoryName;
    private String description;
    private BigDecimal remain;
}
