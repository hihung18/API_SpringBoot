package com.example.api_java.model.dto;

import com.example.api_java.model.entity.UserDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RateDTO {
    @NotNull
    @DecimalMin("0")
    @DecimalMax("5")
    private Integer ratePoint;
    private String rateComment;
    @NotNull
    private Long productProductId;
    @NotNull
    private Long userId;
    @JsonIgnore
    private UserDetail user;

    public String getUserName() {
        return user.getUsername();
    }



}
