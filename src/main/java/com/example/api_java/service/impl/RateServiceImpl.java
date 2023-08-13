package com.example.api_java.service.impl;

import com.example.api_java.exception.EntityPrimaryKeyExistsException;
import com.example.api_java.exception.NotFoundException;
import com.example.api_java.model.dto.RateDTO;
import com.example.api_java.model.dto.UserDetailDTO;
import com.example.api_java.model.embeded.RateId;
import com.example.api_java.model.entity.Product;
import com.example.api_java.model.entity.Rate;
import com.example.api_java.model.entity.UserDetail;
import com.example.api_java.repository.IProductRepository;
import com.example.api_java.repository.IRateRepository;
import com.example.api_java.repository.IUserDetailRepository;
import com.example.api_java.service.IBaseService;
import com.example.api_java.service.IModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class RateServiceImpl implements IBaseService<RateDTO, RateId>, IModelMapper<RateDTO, Rate> {
    private final IRateRepository repository;
    private final ModelMapper modelMapper;
    private final IProductRepository productRepository;
    private final IUserDetailRepository userRepository;

    public RateServiceImpl(IRateRepository repository, ModelMapper modelMapper, IProductRepository productRepository, IUserDetailRepository userRepository) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<RateDTO> findAll() {
        return createFromEntities(repository.findAll());
    }

    public List<RateDTO> findAllbyUserId(Long UserId) {
        return createFromEntities(repository.findAllById_UserId(UserId));
    }

    public List<RateDTO> findAll(Long productId) {
        return createFromEntities(repository.findAllById_Product_ProductIdOrderByRatePoint(productId));
    }

    public RateDTO findById(RateId rateId) {
        Rate entity = repository.findById(rateId);
        return createFromE(entity);
    }

    public RateDTO update(RateId rateId, RateDTO rateDTO) {
        Rate entity = repository.findById(rateId);
        return createFromE(repository.save(updateEntity(entity, rateDTO)));
    }

    public RateDTO save(RateDTO rateDTO) {
        Optional<Rate> entity = repository.findById_Product_ProductIdAndId_User_Id(rateDTO.getProductProductId(), rateDTO.getUserId());
        if (entity.isPresent())
            throw new EntityPrimaryKeyExistsException(Rate.class, rateDTO.getProductProductId() + "-" + rateDTO.getUserId());
        //entity.get().setKey(findKey(rateDTO));
        return createFromE(repository.save(createFromD(rateDTO)));
    }

    public RateDTO delete(RateId rateId) {
        Rate entity = repository.findById(rateId);
        repository.delete(entity);
        return createFromE(entity);
    }

    @Override
    public UserDetailDTO createFromE(UserDetail entity) {
        return null;
    }

    @Override
    public UserDetail updateEntity(UserDetail entity, UserDetailDTO dto) {
        return null;
    }

    public Rate createFromD(RateDTO dto) {
        Rate entity = modelMapper.map(dto, Rate.class);
        entity.setId(findId(dto));
        return entity;
    }

    public RateDTO createFromE(Rate entity) {
        // TypeMap< Rate,RateDTO> typeMap = this.modelMapper.createTypeMap( Rate.class,RateDTO.class);
        //typeMap.addMappings(modelMapper-> {modelMapper.skip(RateDTO::setProductProductId);});
        RateDTO dto = modelMapper.map(entity, RateDTO.class);
        dto.setUser(entity.getId().getUser());
        dto.setProductProductId(entity.getId().getProduct().getProductId());
        dto.setUserId(entity.getId().getUser().getId());
        return dto;
    }

    public Rate updateEntity(Rate entity, RateDTO dto) {
        if (entity != null && dto != null) {
            entity.setRateComment(dto.getRateComment());
            entity.setRatePoint(dto.getRatePoint());
        }
        return entity;
    }

    private RateId findId(RateDTO dto) {
        return findId(dto.getProductProductId(), dto.getUserId());
    }

    public RateDTO findById(Long productId, Long userId) {
        Optional<Rate> entity = Optional.ofNullable(repository.findById_Product_ProductIdAndId_User_Id(productId, userId)
                .orElseThrow(() -> new NotFoundException(Rate.class, productId + "-" + userId)));
        return createFromE(entity.get());
    }

    private RateId findId(Long productId, Long userId) {
        Optional<UserDetail> user = Optional.ofNullable((UserDetail) userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(UserDetail.class, userId)));
        Optional<Product> product = Optional.ofNullable(productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, productId)));
        return new RateId(product.get(), user.get());
    }

    public RateDTO update(Long productId, Long userId, RateDTO dto) {
        Optional<Rate> entity = Optional.ofNullable(repository.findById_Product_ProductIdAndId_User_Id(productId, userId)
                .orElseThrow(() -> new NotFoundException(Rate.class, productId + "-" + userId)));
        entity.get().setId(findId(dto));
        return createFromE(repository.save(updateEntity(entity.get(), dto)));
    }

    public RateDTO delete(Long productId, Long userId) {
        Optional<Rate> entity = Optional.ofNullable(repository.findById_Product_ProductIdAndId_User_Id(productId, userId)
                .orElseThrow(() -> new NotFoundException(Rate.class, productId + "-" + userId)));
        repository.delete(entity.get());
        return createFromE(entity.get());
    }

}
