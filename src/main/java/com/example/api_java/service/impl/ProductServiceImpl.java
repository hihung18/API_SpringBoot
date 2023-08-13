package com.example.api_java.service.impl;

import com.example.api_java.exception.NotFoundException;
import com.example.api_java.model.dto.ProductDTO;
import com.example.api_java.model.dto.UserDetailDTO;
import com.example.api_java.model.entity.*;
import com.example.api_java.repository.ICategoryRepository;
import com.example.api_java.repository.IFeatureRepository;
import com.example.api_java.repository.IImageRepository;
import com.example.api_java.repository.IProductRepository;
import com.example.api_java.service.IBaseService;
import com.example.api_java.service.IModelMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements IBaseService<ProductDTO, Long>, IModelMapper<ProductDTO, Product> {
    private final IProductRepository repository;
    private final ModelMapper modelMapper;
    private final IFeatureRepository featureRepository;
    private final ICategoryRepository categoryRepository;
    private final IImageRepository imageRepository;

    private List<Feature> findAllFeature(Set<Long> featureIds) {
        return featureRepository.findAllByFeaturesId(featureIds);
    }

    public ProductDTO findById(Long id) {
        Optional<Product> entity = repository.findById(id);
        entity.orElseThrow(() -> new NotFoundException(Product.class, id));
        entity.get().setImages(imageRepository.findAllByProductProductId(id));
        return createFromE(entity.get());
    }

    public ProductDTO update(Long id, ProductDTO productDTO) {
        Optional<Product> entity = repository.findById(id);
        entity.orElseThrow(() -> new NotFoundException(Product.class, id));
        return createFromE(repository.save(updateEntity(entity.get(), productDTO)));
    }

    @Transactional
    public ProductDTO save(ProductDTO productDTO) {
        Product entity = createFromD(productDTO);
        Category category = categoryRepository.findById(productDTO.getCateId()).orElseThrow(() -> new NotFoundException(Category.class, ""));
        entity.setCategory(category);
        if (productDTO.getProductCreateDate() == null) {
            entity.setProductCreateDate(new Date());
        }
        repository.save(entity);
        for (int i = 0; i < entity.getImages().size(); i++) {
            Image image = entity.getImages().get(i);
            imageRepository.save(image);
        }
        return createFromE(entity);
    }

    @Transactional
    public ProductDTO delete(Long id) {
        Optional<Product> entity = Optional.ofNullable(repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Product.class, id)));
        imageRepository.deleteAllByProductId(id);

        featureRepository.deleteAllByProductId(id);
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

    public Product createFromD(ProductDTO dto) {
        Product entity = modelMapper.map(dto, Product.class);
        entity.setFeatures(findAllFeature(dto.getFeatureIds()));
        List<Image> images = new ArrayList<>();
        if (dto.getImageUrls() != null) {
            for (int i = 0; i < dto.getImageUrls().size(); i++) {
                Image image = new Image();
                String url = dto.getImageUrls().get(i);
                image.setImageUrl(url);
                image.setProduct(entity);
                images.add(image);
            }
        }
        entity.setImages(images);
        return entity;
    }

    public ProductDTO createFromE(Product entity) {
        ProductDTO dto = modelMapper.map(entity, ProductDTO.class);
        dto.setCategoryName(entity.getCategory().getCategoryName());
        dto.setCateId(entity.getCategory().getCateId());
        try {
            dto.setEventId(entity.getEvent().getEventId());
        } catch (Exception e) {

        }
        if (entity.getFeatures() != null || entity.getFeatures().size() != 0) {
            dto.setFeaturesName(entity.getFeatures().stream().map(Feature::getFeatureSpecific).collect(Collectors.joining(" ")));
            dto.setFeatureIds(entity.getFeatures().stream().map(Feature::getFeatureFeatureId).collect(Collectors.toSet()));
            dto.setFeatureTypes(entity.getFeatures().stream().map((e) -> e.getFeatureType().getFeatureTypeName()).collect(Collectors.toSet()));
            dto.setFeatureTypeId(entity.getFeatures().stream().map((e) -> e.getFeatureType().getFeatureTypeId()).collect(Collectors.toSet()));
            dto.setFeatureSpecific(entity.getFeatures().stream().map(Feature::getFeatureSpecific).collect(Collectors.toList()));
            dto.setFeaturePoint(entity.getFeatures().stream().map(Feature::getFeaturePoint).collect(Collectors.toList()));
        }
        if (entity.getImages() != null)
            dto.setImageUrls(entity.getImages().stream().map(Image::getImageUrl).collect(Collectors.toList()));
        return dto;
    }

    public Product updateEntity(Product entity, ProductDTO dto) {
        if (entity != null && dto != null) {
            entity.setProductDescription(dto.getProductDescription());
            entity.setProductPrice(dto.getProductPrice());
            entity.setProductRemain(dto.getProductRemain());
            entity.setProductName(dto.getProductName());
            entity.setFeatures(findAllFeature(dto.getFeatureIds()));
            entity.setCategory(categoryRepository.findById(dto.getCateId())
                    .orElseThrow(() -> new NotFoundException(Category.class, dto.getCateId())));
            List<Image> images = new ArrayList<>();
            if (dto.getImageUrls() != null) {
                for (Image image : imageRepository.findAllByProductProductId(dto.getProductId())) {
                    imageRepository.delete(image);
                }
                for (int i = 0; i < dto.getImageUrls().size(); i++) {
                    Image image = new Image();
                    String url = dto.getImageUrls().get(i);
                    image.setImageUrl(url);
                    image.setProduct(entity);
                    images.add(image);
                    imageRepository.save(image);
                }
            }
            entity.setImages(images);
        }
        return entity;
    }

    public List<ProductDTO> findAll(Long categoryId, Set<Long> featureIds) {
        List<Product> products = repository.findAllByFilter(featureIds, categoryId);
        setImage(products);
        List<ProductDTO> productDTOS = createFromEntities(products);
        return productDTOS;
    }

    public List<ProductDTO> findAll(Long categoryId) {
        List<Product> products = repository.findAllByCategoryCateId(categoryId);
        setImage(products);
        List<ProductDTO> productDTOS = createFromEntities(products);
        return productDTOS;
    }

    public List<ProductDTO> findAllEvent(Long eventId) {
        List<Product> products = repository.findAllByEvent_EventId(eventId);
        setImage(products);
        List<ProductDTO> productDTOS = createFromEntities(products);
        return productDTOS;
    }


    public List<ProductDTO> findAllBySetProducts(Set<Long> productIds) {
        List<Product> products = repository.findAllByProductId(productIds);
        setImage(products);
        List<ProductDTO> productDTOS = createFromEntities(products);
        return productDTOS;
    }

    public List<ProductDTO> findAll(Set<Long> featureIds) {
        List<Product> products = repository.findAllByFeaturesId(featureIds);
        setImage(products);
        List<ProductDTO> productDTOS = createFromEntities(products);
        return productDTOS;
    }
    public List<ProductDTO> findAll() {
        List<Product> products = repository.findAll();
        setImage(products);
        List<ProductDTO> productDTOS = createFromEntities(products);
        return productDTOS;
    }

    private void setImage(final List<Product> products) {
        for (Product product : products) {
            product.setImages(imageRepository.findAllByProductProductId(product.getProductId()));
        }
    }
}
