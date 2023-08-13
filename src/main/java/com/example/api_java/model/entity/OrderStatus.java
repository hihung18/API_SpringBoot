package com.example.api_java.model.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CART, PREPARE, SHIPPING, SUCCESS, CONFIRM, CANCELED;
    //CART trong giỏ hàng
    // sẵn sàn bán (set khi thằng admin thêm sp, ý là sản phẩm sẵn sàng để bán
    // ship đang ship
    //SUCCESS đặt hàng thành công
// CONFIRM shop xác nhận đơn hàng
// CANCELED hủy đơn
}
