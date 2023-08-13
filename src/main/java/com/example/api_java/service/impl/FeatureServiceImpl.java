package com.example.api_java.service.impl;

import com.example.api_java.exception.NotFoundException;
import com.example.api_java.model.dto.FeatureDTO;
import com.example.api_java.model.dto.UserDetailDTO;
import com.example.api_java.model.entity.Feature;
import com.example.api_java.model.entity.FeatureType;
import com.example.api_java.model.entity.UserDetail;
import com.example.api_java.repository.IFeatureRepository;
import com.example.api_java.repository.IFeatureTypeRepository;
import com.example.api_java.service.IBaseService;
import com.example.api_java.service.IModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class FeatureServiceImpl implements IBaseService<FeatureDTO, Long>, IModelMapper<FeatureDTO, Feature> {
  private final IFeatureRepository repository;

  private final ModelMapper modelMapper;

  private final IFeatureTypeRepository typeRepository;

  public FeatureServiceImpl(IFeatureRepository repository, ModelMapper modelMapper, IFeatureTypeRepository typeRepository) {
    this.repository = repository;
    this.modelMapper = modelMapper;
    this.typeRepository = typeRepository;
  }

  @Override
  public List<FeatureDTO> findAll() {
    return createFromEntities(repository.findAll());
  }

  @Override
  public FeatureDTO findById(Long id) {
    Optional<Feature> entity = repository.findById(id);
    entity.orElseThrow(() -> new NotFoundException(Feature.class, id));
    return createFromE(entity.get());
  }

  @Override
  public FeatureDTO update(Long id, FeatureDTO dto) {
    Optional<Feature> entity = repository.findById(id);
    entity.orElseThrow(() -> new NotFoundException(Feature.class, id));
    return createFromE(repository.save(updateEntity(entity.get(), dto)));
  }

  @Override
  public FeatureDTO save(FeatureDTO dto) {
    return createFromE(repository.save(createFromD(dto)));
  }

  @Override
  public FeatureDTO delete(Long id) {
    Optional<Feature> entity = Optional.ofNullable(repository.findById(id)
            .orElseThrow(() -> new NotFoundException(Feature.class, id)));
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

  @Override
  public Feature createFromD(FeatureDTO dto) {
    Feature entity = modelMapper.map(dto, Feature.class);
    entity.setFeatureType(typeRepository.findById(dto.getFeatureTypeId()).get());
    return entity;
  }

  @Override
  public FeatureDTO createFromE(Feature entity) {
    FeatureDTO dto = modelMapper.map(entity, FeatureDTO.class);
    dto.setFeatureTypeId(entity.getFeatureType().getFeatureTypeId());
    return dto;
  }

  @Override
  public Feature updateEntity(Feature entity, FeatureDTO dto) {
    if (entity != null && dto != null) {
      entity.setFeatureType(typeRepository.findById(dto.getFeatureTypeId())
              .orElseThrow(() -> new NotFoundException(FeatureType.class, dto.getFeatureFeatureId())));
      entity.setFeatureSpecific(dto.getFeatureSpecific());
      //entity.setFeatureId(dto.getFeatureId());

    }

    return entity;
  }

  public List<FeatureDTO> findAllft(Long featureTypeId) {
    return createFromEntities(repository.findAllByFeatureType_FeatureTypeId(featureTypeId));
  }

  public List<FeatureDTO> findAll(Long productId) {
    return createFromEntities(repository.findAllByProductId(productId));
  }
}
