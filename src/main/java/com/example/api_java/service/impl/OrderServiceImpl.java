package com.example.api_java.service.impl;

import com.example.api_java.exception.NotFoundException;
import com.example.api_java.model.dto.OrderDTO;
import com.example.api_java.model.dto.OrderDetailDTO;
import com.example.api_java.model.dto.OrderStatusClass;
import com.example.api_java.model.dto.UserDetailDTO;
import com.example.api_java.model.entity.*;
import com.example.api_java.repository.IOrderDetailRepository;
import com.example.api_java.repository.IOrderRepository;
import com.example.api_java.repository.IUserDetailRepository;
import com.example.api_java.service.IBaseService;
import com.example.api_java.service.IModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderServiceImpl implements IBaseService<OrderDTO, Long>, IModelMapper<OrderDTO, Order> {
  private final IOrderRepository repository;
  private final ModelMapper modelMapper;
  private final IOrderDetailRepository orderDetailRepository;
  private final OrderDetailServiceImpl orderDetailService;
  private final IUserDetailRepository userRepository;

  public OrderServiceImpl(IOrderRepository repository, ModelMapper modelMapper, IOrderDetailRepository orderDetailRepository, OrderDetailServiceImpl orderDetailService, IUserDetailRepository userRepository) {
    this.repository = repository;
    this.modelMapper = modelMapper;
    this.orderDetailRepository = orderDetailRepository;
    this.orderDetailService = orderDetailService;
    this.userRepository = userRepository;
  }

  public List<OrderStatusClass> findOrderStatus() {
    List<OrderStatusClass> orderStatusClasses = new ArrayList<>();
    for (int i = 0; i < 6; i++) {
      OrderStatusClass os = new OrderStatusClass();
      if (i == 0) os.setOrderStatus(OrderStatus.CART);
      if (i == 1) os.setOrderStatus(OrderStatus.PREPARE);
      if (i == 2) os.setOrderStatus(OrderStatus.SHIPPING);
      if (i == 3) os.setOrderStatus(OrderStatus.SUCCESS);
      if (i == 4) os.setOrderStatus(OrderStatus.CONFIRM);
      if (i == 5) os.setOrderStatus(OrderStatus.CANCELED);
      os.setAmountOrderStatus(BigDecimal.valueOf(0));
      os.setId(Long.valueOf(i + 1));
      orderStatusClasses.add(os);
    }
    for (Order order : repository.findAll()) {
      for (OrderStatusClass s : orderStatusClasses) {
        if (order.getOrderStatus().equals(s.getOrderStatus())) {
          s.setAmountOrderStatus(s.getAmountOrderStatus().add(BigDecimal.valueOf(1)));
          break;
        }
      }
    }
    return orderStatusClasses;
  }

  public List<OrderDTO> findAll() {
    List<OrderDTO> dtos = this.createFromEntities(this.repository.findAll());
    return dtos;
  }

  public List<OrderDetailView> findAllView(Long userId) {
    return this.repository.getAllOrderByUserId(userId);
  }

  public List<OrderDetailView> findAllView() {
    return this.repository.getAllOrder();
  }

  public List<OrderDTO> findAll(Long userId) {
    return this.createFromEntities(this.repository.findByUser_Id(userId));
  }

  public OrderDTO findById(Long id) {
    Order entity = repository.findById(id)
            .orElseThrow(() -> new NotFoundException(Order.class, id));
    return createFromE(entity);
  }

  public OrderDTO update(Long id, OrderDTO dto) {
    Optional<Order> entity = repository.findById(id);
    entity.orElseThrow(() -> new NotFoundException(Order.class, id));
    return createFromE(repository.save(updateEntity(entity.get(), dto)));
  }

  public OrderDTO save(OrderDTO dto) {
    Date date = new Date();
    Order entity = createFromD(dto);
    //if(t.getTime()==null)
    entity.setOrderTime(date);
    entity.setOrderStatus(dto.getOrderStatus());
    entity.setUser(userRepository.getById(dto.getUserId()));
    repository.save(entity);

    entity = repository.findByOrderTime(date).get();
    OrderDetailDTO detail = null;
    // OrderDetailKey key = null;
    for (Map.Entry<Long, Integer> e : dto.getOrderDetails().entrySet()) {
      detail = new OrderDetailDTO();
      detail.setProductId(e.getKey());
      detail.setOrderId(entity.getOrderId());
      detail.setAmount(e.getValue());
      orderDetailService.save(detail);

    }
    return createFromE(entity);
  }

  public OrderDTO delete(Long id) {
    Optional<Order> entity = Optional.ofNullable(repository.findById(id)
            .orElseThrow(() -> new NotFoundException(Order.class, id)));
    List<OrderDetail> orderDetails = orderDetailRepository.findById_Order_OrderId(id);
    for (OrderDetail od : orderDetails) {
      orderDetailRepository.delete(od);
    }
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

  public Order createFromD(OrderDTO dto) {
    Optional<Order> entity = Optional.ofNullable(modelMapper.map(dto, Order.class));
    entity.orElseThrow(() -> new NotFoundException(Order.class, dto.getOrderId()));
    entity.get().setUser( userRepository.getById(dto.getUserId()));
    return entity.get();
  }

  public OrderDTO createFromE(Order entity) {
    OrderDTO dto = modelMapper.map(entity, OrderDTO.class);
    dto.setUserId(entity.getUser().getId());
    dto.setUserId(entity.getUser().getId());
    entity.setOrderDetails(orderDetailRepository.findById_Order_OrderId(entity.getOrderId()));
    Map<Long, Integer> map = new HashMap<>();
    for (int i = 0; i < entity.getOrderDetails().size(); i++) {
      map.put(entity.getOrderDetails().get(i).getId().getProduct().getProductId(),
              entity.getOrderDetails().get(i).getAmount());
      dto.setOrderDetails(map);
    }
    return dto;
  }

  public Order updateEntity(Order entity, OrderDTO dto) {
    if (entity != null && dto != null) {
      entity.setOrderStatus(dto.getOrderStatus());
      entity.setOrderPhone(dto.getOrderPhone());
      entity.setOrderAddress(dto.getOrderAddress());
      List<OrderDetail> orderDetails = new ArrayList<>();
      orderDetails = orderDetailRepository.findById_Order_OrderId(dto.getOrderId());
      for (OrderDetail orderDetail : orderDetails) {
        orderDetail.setAmount(dto.getOrderDetails().get(orderDetail.getId().getProduct().getProductId()));
      }
      orderDetailRepository.saveAll(orderDetails);
    }

    return entity;
  }
}
