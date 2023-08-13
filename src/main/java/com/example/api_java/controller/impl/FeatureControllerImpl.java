package com.example.api_java.controller.impl;

import com.example.api_java.controller.IBaseController;
import com.example.api_java.model.dto.FeatureDTO;
import com.example.api_java.service.impl.FeatureServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping("api/features")
@Tag(name = "Features")
public class FeatureControllerImpl implements IBaseController<FeatureDTO, Long, FeatureServiceImpl> {
    @Resource
    @Getter
    private FeatureServiceImpl service;


    @GetMapping("")
    public List<FeatureDTO> getAll(@RequestParam(required = false) Long featureTypeId, @RequestParam(required = false) Long productId) {
        if (featureTypeId != null) {
            return getService().findAllft(featureTypeId);
        } else if (productId != null) {
            return getService().findAll(productId);
        }

        return getService().findAll();
    }

}