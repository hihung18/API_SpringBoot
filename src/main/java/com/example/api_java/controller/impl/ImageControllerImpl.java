package com.example.api_java.controller.impl;

import com.example.api_java.controller.IBaseController;
import com.example.api_java.model.dto.ImageDTO;
import com.example.api_java.service.impl.ImageServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping({"api/images"})
@Tag(
        name = "Image"
)
public class ImageControllerImpl implements IBaseController<ImageDTO, Long, ImageServiceImpl> {
    @Resource
    @Getter
    private ImageServiceImpl service;

    @GetMapping("")
    public List<ImageDTO> getAll(@RequestParam(required = false) Long productId) {
        if (productId != null)
            return getService().findAll(productId);
        else
            return getService().findAll();
    }
}
