package com.example.api_java.service.impl;

import com.example.api_java.exception.EntityPrimaryKeyExistsException;
import com.example.api_java.exception.NotFoundException;
import com.example.api_java.exception.NumberErrorException;
import com.example.api_java.model.dto.OrderDetailDTO;
import com.example.api_java.model.dto.UserDetailDTO;
import com.example.api_java.model.embeded.OrderDetailId;
import com.example.api_java.model.entity.Order;
import com.example.api_java.model.entity.OrderDetail;
import com.example.api_java.model.entity.Product;
import com.example.api_java.model.entity.UserDetail;
import com.example.api_java.repository.IOrderDetailRepository;
import com.example.api_java.repository.IOrderRepository;
import com.example.api_java.repository.IProductRepository;
import com.example.api_java.service.IBaseService;
import com.example.api_java.service.IModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailServiceImpl implements IBaseService<OrderDetailDTO, OrderDetailId>, IModelMapper<OrderDetailDTO, OrderDetail> {
    private final IOrderDetailRepository repository;
    private final ModelMapper modelMapper;
    private final IProductRepository productRepository;
    private final IOrderRepository orderRepository;

    public OrderDetailServiceImpl(IOrderDetailRepository repository, ModelMapper modelMapper, IProductRepository productRepository, IOrderRepository orderRepository) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public List<OrderDetailDTO> findAll() {
        return createFromEntities(repository.findAll());
    }

    public List<OrderDetailDTO> findAll(Long orderId) {
        return createFromEntities(repository.findById_Order_OrderId(orderId));
    }

    public List<OrderDetailDTO> findAll(Long orderId, Long productId) {
        return createFromEntities((Collection<OrderDetail>)
                repository.findById_Product_ProductIdAndId_Order_OrderId(productId, orderId
                ).orElseThrow(() -> new NotFoundException(OrderDetailDTO.class, orderId)));
    }

    public OrderDetailDTO findById(OrderDetailId orderDetailId) {
        Optional<OrderDetail> entity = repository.findById(orderDetailId);
        entity.orElseThrow(() -> new NotFoundException(OrderDetail.class, orderDetailId.getOrder().getOrderId()));
        return createFromE(entity.get());
    }

    public OrderDetailDTO update(OrderDetailId orderDetailId, OrderDetailDTO orderDetailDTO) {
        Optional<OrderDetail> entity = repository.findById(orderDetailId);
        entity.orElseThrow(() -> new NotFoundException(OrderDetail.class, orderDetailId.getOrder().getOrderId()));
        return createFromE(repository.save(updateEntity(entity.get(), orderDetailDTO)));
    }

    @Transactional
    public OrderDetailDTO save(OrderDetailDTO orderDetailDTO) {
        Optional<OrderDetail> entity = repository.findById_Product_ProductIdAndId_Order_OrderId(orderDetailDTO.getProductId(), orderDetailDTO.getOrderId());
        if (entity.isPresent())
            throw new EntityPrimaryKeyExistsException(OrderDetail.class, orderDetailDTO.getProductId() + "-" + orderDetailDTO.getOrderId());
        minusRemainProduct(orderDetailDTO, 0);
        return createFromE(repository.save(createFromD(orderDetailDTO)));
    }

    private void minusRemainProduct(OrderDetailDTO orderDetailDTO, int currentNum) {
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new NotFoundException(Product.class, orderDetailDTO.getProductId()));
        int remain = product.getProductRemain();
        if (remain < (orderDetailDTO.getAmount() - currentNum))
            throw new NumberErrorException("Number of product " + product.getProductId() + " not enough");
        product.setProductRemain(remain - (orderDetailDTO.getAmount() - currentNum));
        productRepository.save(product);
    }

    public OrderDetailDTO delete(OrderDetailId orderDetailId) {
        Optional<OrderDetail> entity = Optional.ofNullable(repository.findById(orderDetailId)
                .orElseThrow(() -> new NotFoundException(OrderDetail.class, "id")));
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

    private OrderDetailId findKey(OrderDetailDTO dto) {
        return findId(dto.getProductId(), dto.getOrderId());

    }

    public OrderDetail createFromD(OrderDetailDTO dto) {
        OrderDetail entity = modelMapper.map(dto, OrderDetail.class);
        entity.setId(findKey(dto));
//        entity.se
        return entity;
    }

    public OrderDetailDTO createFromE(OrderDetail entity) {
        OrderDetailDTO dto = modelMapper.map(entity, OrderDetailDTO.class);
        dto.setOrderId(entity.getId().getOrder().getOrderId());
        dto.setProductId(entity.getId().getProduct().getProductId());
        return dto;
    }

    public OrderDetail updateEntity(OrderDetail entity, OrderDetailDTO dto) {
        if (entity != null && dto != null) {
            entity.setAmount(dto.getAmount());
            //entity.setKey(dto.getUnit());
            //entity.setId(dto.getId());

        }

        return entity;
    }

    public OrderDetailDTO findById(Long productId, Long orderId) {
        Optional<OrderDetail> orderDetail = Optional.ofNullable(repository.findById_Product_ProductIdAndId_Order_OrderId(productId, orderId)
                .orElseThrow(() -> new NotFoundException(OrderDetail.class, productId + "-" + orderId)));
        return createFromE(orderDetail.get());
    }

    public OrderDetailDTO update(Long productId, Long orderId, OrderDetailDTO orderDetailDTO) {
        Optional<OrderDetail> entity = Optional.ofNullable(repository.findById_Product_ProductIdAndId_Order_OrderId(productId, orderId)
                .orElseThrow(() -> new NotFoundException(OrderDetail.class, productId + "-" + orderId)));
        entity.get().setId(findId(productId, orderId));
        int currentNum = entity.get().getAmount();
        minusRemainProduct(orderDetailDTO, currentNum);
        return createFromE(repository.save(updateEntity(entity.get(), orderDetailDTO)));
    }

    private OrderDetailId findId(Long productId, Long orderId) {
        Optional<Order> order = Optional.ofNullable(orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(Order.class, orderId)));
        Optional<Product> product = Optional.ofNullable(productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(Product.class, productId)));
        return new OrderDetailId(order.get(), product.get());
    }

    public OrderDetailDTO delete(Long productId, Long orderId) {
        Optional<OrderDetail> entity = Optional.ofNullable(repository.findById_Product_ProductIdAndId_Order_OrderId(productId, orderId)
                .orElseThrow(() -> new NotFoundException(OrderDetail.class, productId + "-" + orderId)));
        repository.delete(entity.get());
        return createFromE(entity.get());
    }
}
