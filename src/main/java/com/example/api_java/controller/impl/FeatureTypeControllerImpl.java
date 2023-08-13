package com.example.api_java.controller.impl;

import com.example.api_java.controller.IBaseController;
import com.example.api_java.controller.IGetController;
import com.example.api_java.model.dto.FeatureTypeDTO;
import com.example.api_java.service.impl.FeatureTypeServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import lombok.Getter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin("*")
@RestController
@RequestMapping({"api/featureTypes"})
@Tag(
        name = "FeatureType"
)
public class FeatureTypeControllerImpl implements IBaseController<FeatureTypeDTO, Long, FeatureTypeServiceImpl>
        , IGetController<FeatureTypeDTO, Long, FeatureTypeServiceImpl> {
    @Resource
    @Getter
    private FeatureTypeServiceImpl service;

}
