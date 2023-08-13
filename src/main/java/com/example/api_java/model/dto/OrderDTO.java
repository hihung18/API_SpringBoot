package com.example.api_java.model.dto;

import com.example.api_java.model.entity.OrderStatus;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

@Setter
@Getter
public class OrderDTO {
    private Long orderId;
    @NotNull
    private Long userId;

    private String orderPhone;
    @NotNull
    private String orderAddress;

    private OrderStatus orderStatus;
    private Date orderTime;
    @NotNull
    private Map<Long, Integer> orderDetails;
}
