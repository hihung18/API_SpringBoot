package com.example.api_java.model.dto;

import com.example.api_java.model.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusClass {
    Long id;
    OrderStatus orderStatus;
    BigDecimal amountOrderStatus;
}
