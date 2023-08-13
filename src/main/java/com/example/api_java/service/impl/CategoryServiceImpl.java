package com.example.api_java.service.impl;

import com.example.api_java.exception.NotFoundException;
import com.example.api_java.model.dto.CateDTO;
import com.example.api_java.model.dto.UserDetailDTO;
import com.example.api_java.model.entity.Category;
import com.example.api_java.model.entity.Product;
import com.example.api_java.model.entity.UserDetail;
import com.example.api_java.repository.ICategoryRepository;
import com.example.api_java.service.IBaseService;
import com.example.api_java.service.IModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements IBaseService<CateDTO, Long>, IModelMapper<CateDTO, Category> {

    private final ICategoryRepository repository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(ICategoryRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CateDTO> findAll() {
        return createFromEntities(repository.findAll());
    }
    @Override
    public CateDTO findById(Long id) {
        Optional<Category> cate = repository.findById(id);
        cate.orElseThrow(() -> new NotFoundException(Category.class, id));
        return createFromE(cate.get());
    }

    @Override
    public CateDTO update(Long id, CateDTO dto) {
        Optional<Category> entity = repository.findById(id);
        entity.orElseThrow(() -> new NotFoundException(Category.class, id));
        return createFromE(repository.save(updateEntity(entity.get(), dto)));
    }

    @Override
    public CateDTO save(CateDTO entity) {
        return createFromE(repository.save(createFromD(entity)));
    }

    @Override
    public CateDTO delete(Long id) {
        Optional<Category> category = Optional.ofNullable(repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Category.class, id)));
        repository.delete(category.get());
        return createFromE(category.get());
    }

    @Override
    public UserDetailDTO createFromE(UserDetail entity) {
        return null;
    }

    @Override
    public UserDetail updateEntity(UserDetail entity, UserDetailDTO dto) {
        return null;
    }

    @Override
    public Category createFromD(CateDTO dto) {
        Category entity = modelMapper.map(dto, Category.class);
        entity.setProducts((List<Product>) repository.findAllProductByCate(dto.getCateId()));
        return entity;
    }

    @Override
    public CateDTO createFromE(Category entity) {
        CateDTO dto = modelMapper.map(entity, CateDTO.class);
        try {
            BigDecimal sum = BigDecimal.valueOf(Long.valueOf(0));
            for (Product product : entity.getProducts()) {
                BigDecimal productRemain = BigDecimal.valueOf(product.getProductRemain());
                sum = sum.add(productRemain);
            }
            dto.setRemain(sum);
        } catch (Exception e) {

        }
        return dto;
    }

    @Override
    public Category updateEntity(Category entity, CateDTO dto) {
        if (entity != null && dto != null) {
            entity.setCategoryName(dto.getCategoryName());
            entity.setDescription(dto.getDescription());
            //entity.setId(dto.getId());
        }
        return entity;
    }
}
