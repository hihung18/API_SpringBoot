package com.example.api_java.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@Schema(name = "EventDTO")
public class EventDTO {
    private Long eventId;
    @NotNull
    private String eventName;
    private Float discount;
    private String image;
}
