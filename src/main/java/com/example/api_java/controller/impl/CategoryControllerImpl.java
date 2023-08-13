package com.example.api_java.controller.impl;

import com.example.api_java.controller.IBaseController;
import com.example.api_java.controller.IGetController;
import com.example.api_java.model.dto.CateDTO;
import com.example.api_java.service.impl.CategoryServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.Getter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@CrossOrigin("*")
@RestController
@RequestMapping("api/categories")
@Tag(name = "Categories")
public class CategoryControllerImpl implements IBaseController<CateDTO, Long, CategoryServiceImpl>
        , IGetController<CateDTO, Long, CategoryServiceImpl> {
  @Resource
  @Getter
  private CategoryServiceImpl service;
}
