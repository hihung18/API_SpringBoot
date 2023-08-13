package com.example.api_java.controller.impl;

import com.example.api_java.model.dto.OrderDetailDTO;
import com.example.api_java.service.impl.OrderDetailServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping({"api/orderDetails"})
@Tag(
        name = "OrderDetail"
)
public class OrderDetailControllerImpl {//implements IBaseController<OrderDetailDTO, OrderDetailKey, OrderDetailServiceImpl> {
    @Resource
    @Getter
    private OrderDetailServiceImpl service;

    @GetMapping("")
    public List<OrderDetailDTO> getAll(@RequestParam(required = false) Long orderId) {
        if (orderId == null)
            return getService().findAll();
        else
            return getService().findAll(orderId);
    }

    @GetMapping("/{productId}-{orderId}")
    public OrderDetailDTO get1(@PathVariable Long productId, @PathVariable Long orderId) {

        return service.findById(productId, orderId);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{productId}-{orderId}")
    public OrderDetailDTO update(@PathVariable Long productId, @PathVariable Long orderId, @Valid @RequestBody OrderDetailDTO dto) {
        dto.setProductId(productId);
        dto.setOrderId(orderId);
        return service.update(productId, orderId, dto);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{productId}-{orderId}")
    public OrderDetailDTO delete(@PathVariable Long productId, @PathVariable Long orderId) {
        return service.delete(productId, orderId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public OrderDetailDTO insert(@Valid @RequestBody OrderDetailDTO d) {
        return getService().save(d);
    }
}

