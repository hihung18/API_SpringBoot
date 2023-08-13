package com.example.api_java.controller.impl;

import com.example.api_java.controller.IBaseController;
import com.example.api_java.model.dto.ProductDTO;
import com.example.api_java.service.impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin("*")
@RestController
@RequestMapping({"api/products"})
@Tag(
        name = "Product"
)
public class ProductControllerImpl implements IBaseController<ProductDTO, Long, ProductServiceImpl> {
    @Resource
    @Getter
    private ProductServiceImpl service;

    @GetMapping("")
    public List<ProductDTO> getAll(@RequestParam(required = false) Long categoryId,
                                   @RequestParam(required = false) Set<Long> featureIds,
                                   @RequestParam(required = false) Set<Long> products,
                                   @RequestParam(required = false) Long eventId) {
        if (products != null) {
            return getService().findAllBySetProducts(products);
        }
        if (eventId != null) {
            return getService().findAllEvent(eventId);
        }
        if (categoryId != null && featureIds != null) {
            //FilterProductRequest filterProductRequest = new FilterProductRequest(categoryId,featureIds);
            return getService().findAll(categoryId, featureIds);
        } else if (categoryId != null) {
            return getService().findAll(categoryId);
        } else if (featureIds != null) {
            return getService().findAll(featureIds);
        } else {
            return getService().findAll();
        }
    }
}