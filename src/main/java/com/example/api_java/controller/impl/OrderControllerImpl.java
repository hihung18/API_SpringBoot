package com.example.api_java.controller.impl;

import com.example.api_java.controller.IBaseController;
import com.example.api_java.model.dto.OrderDTO;
import com.example.api_java.model.dto.OrderStatusClass;
import com.example.api_java.service.impl.OrderServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping("api/orders")
@Tag(
        name = "Orders"
)
public class OrderControllerImpl implements IBaseController<OrderDTO, Long, OrderServiceImpl> {
    @Resource
    @Getter
    private OrderServiceImpl service;

    @GetMapping("")
    public List<OrderDTO> getAll(@RequestParam(required = false) Long userId) {
        if (userId == null)
            return getService().findAll();
        else
            return getService().findAll(userId);
    }

    @GetMapping("/status")
    public List<OrderStatusClass> getOrderStatus() {
        return getService().findOrderStatus();
    }

}

