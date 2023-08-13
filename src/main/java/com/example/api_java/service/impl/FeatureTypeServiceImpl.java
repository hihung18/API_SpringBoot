package com.example.api_java.service.impl;

import com.example.api_java.exception.NotFoundException;
import com.example.api_java.model.dto.FeatureTypeDTO;
import com.example.api_java.model.dto.UserDetailDTO;
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
public class FeatureTypeServiceImpl implements IBaseService<FeatureTypeDTO, Long>, IModelMapper<FeatureTypeDTO, FeatureType> {
    private final IFeatureTypeRepository repository;
    private final ModelMapper modelMapper;
    private final IFeatureRepository featureRepository;

    public FeatureTypeServiceImpl(IFeatureTypeRepository repository, ModelMapper modelMapper, IFeatureRepository featureRepository) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.featureRepository = featureRepository;
    }

    @Override
    public List<FeatureTypeDTO> findAll() {
        return createFromEntities(repository.findAll());
    }

    @Override
    public FeatureTypeDTO findById(Long id) {
        FeatureType entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(FeatureType.class, id));
        return createFromE(entity);
    }

    @Override
    public FeatureTypeDTO save(FeatureTypeDTO dto) {
        return createFromE(repository.save(createFromD(dto)));
    }

    @Override
    public FeatureTypeDTO update(Long id, FeatureTypeDTO dto) {
        Optional<FeatureType> entity = repository.findById(id);
        entity.orElseThrow(() -> new NotFoundException(FeatureType.class, id));
        return createFromE(repository.save(updateEntity(entity.get(), dto)));
    }

    @Override
    public FeatureTypeDTO delete(Long id) {
        Optional<FeatureType> entity = Optional.ofNullable(repository.findById(id)
                .orElseThrow(() -> new NotFoundException(FeatureType.class, id)));
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
    public FeatureType createFromD(FeatureTypeDTO dto) {
        FeatureType entity = modelMapper.map(dto, FeatureType.class);
        return entity;
    }

    public FeatureTypeDTO createFromE(FeatureType entity) {
        FeatureTypeDTO dto = modelMapper.map(entity, FeatureTypeDTO.class);
        return dto;

    }

    public FeatureType updateEntity(FeatureType entity, FeatureTypeDTO dto) {
        if (entity != null && dto != null) {
            entity.setFeatureTypeName(dto.getFeatureTypeName());
            entity.setFeatureTypeUnit(dto.getFeatureTypeUnit());
            //entity.setId(dto.getId());

        }

        return entity;
    }
}
