package com.example.api_java.repository;

import com.example.api_java.model.embeded.OrderDetailId;
import com.example.api_java.model.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {
    @Query(
            value = "select * " + "from orderdetails od" + " where od.order_id=?2 and od.product_id=?1",
            nativeQuery = true)
    OrderDetail findByProductIdAndOrderId(Long productId, Long orderId);


    Optional<OrderDetail> findById_Product_ProductIdAndId_Order_OrderId(Long productid, Long orderid);


    List<OrderDetail> findById_Order_OrderId(Long orderId);

}
