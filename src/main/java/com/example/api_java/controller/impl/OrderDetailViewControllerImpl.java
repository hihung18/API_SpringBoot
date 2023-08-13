package com.example.api_java.controller.impl;

import com.example.api_java.model.entity.OrderDetailView;
import com.example.api_java.service.impl.OrderServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping("api/orderDetailViews")
@Tag(
        name = "OrderDetailViews"
)
public class OrderDetailViewControllerImpl {
    @Resource
    @Getter
    private OrderServiceImpl service;

    @GetMapping("")
    public List<OrderDetailView> getAll(@RequestParam(required = false) Long userId) {
        if (userId != null)
            return getService().findAllView(userId);
        else
            return getService().findAllView();
    }
}
