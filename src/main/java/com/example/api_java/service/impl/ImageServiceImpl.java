package com.example.api_java.service.impl;

import com.example.api_java.exception.NotFoundException;
import com.example.api_java.model.dto.ImageDTO;
import com.example.api_java.model.dto.UserDetailDTO;
import com.example.api_java.model.entity.Image;
import com.example.api_java.model.entity.Product;
import com.example.api_java.model.entity.UserDetail;
import com.example.api_java.repository.IImageRepository;
import com.example.api_java.repository.IProductRepository;
import com.example.api_java.service.IBaseService;
import com.example.api_java.service.IModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements IBaseService<ImageDTO, Long>, IModelMapper<ImageDTO, Image> {
    private final IImageRepository repository;
    private final ModelMapper modelMapper;
    private final IProductRepository productRepository;

    public ImageServiceImpl(IImageRepository repository, ModelMapper modelMapper, IProductRepository productRepository) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
    }

    public List<ImageDTO> findAll() {
        return createFromEntities(repository.findAll());
    }

    public List<ImageDTO> findAll(Long productId) {
        return createFromEntities(repository.findAllByProductProductId(productId));
    }

    public ImageDTO findById(Long imageId) {
        return createFromE(repository.findById(imageId).get());
    }


    public ImageDTO save(ImageDTO dto) {
        return createFromE(repository.save(createFromD(dto)));
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public ImageDTO delete(Long id) {
        Optional<Image> entity = Optional.ofNullable(repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Image.class, id)));
        repository.delete(entity.get());
        return createFromE(entity.get());
    }

    @Override
    public UserDetailDTO createFromE(UserDetail entity) {
        return null;
    }

    @Override
    public UserDetail updateEntity(UserDetail entity, UserDetailDTO dto) {
        return null;
    }

    public ImageDTO update(Long id, ImageDTO dto) {
        Optional<Image> entity = repository.findById(id);
        entity.orElseThrow(() -> new NotFoundException(Image.class, id));
        return createFromE(repository.save(updateEntity(entity.get(), dto)));
    }

    public Image createFromD(ImageDTO dto) {
        Image entity = modelMapper.map(dto, Image.class);
        entity.setProduct(productRepository.findById(dto.getProductId()).get());
        return entity;
    }

    public ImageDTO createFromE(Image entity) {
        ImageDTO dto = modelMapper.map(entity, ImageDTO.class);
        dto.setProductId(entity.getProduct().getProductId());
        return dto;
    }

    public Image updateEntity(Image entity, ImageDTO dto) {
        if (entity != null && dto != null) {
            entity.setImageAlt(dto.getImageAlt());
            entity.setImageHeight(dto.getImageHeight());
            //entity.setId(dto.getId());
            entity.setProduct(productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new NotFoundException(Product.class, dto.getProductId())));
            entity.setImageUrl(dto.getImageUrl());
            entity.setImageWidth(dto.getImageWidth());

        }

        return entity;
    }


}
