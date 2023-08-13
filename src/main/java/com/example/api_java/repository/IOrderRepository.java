package com.example.api_java.repository;

import com.example.api_java.model.entity.Order;
import com.example.api_java.model.entity.OrderDetailView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    @Query(
            value =
                    "select o.orderId as orderId, o.status as status, u.username as username, u.email as email, u.first_name as firstName, u.last_name as lastName, u.address as uAddress, o.address as address,"
                            + " o.phone as phone, od.amount as amount, p.id as productId, p.name as name, p.price as price, p.remain as remain,"
                            + " (select top(1) i.url from image i where i.product_id=p.id) as url"
                            + " from users u left join orders o on u.id=o.user_id"
                            + " join orderdetails od on o.orderid=od.order_id"
                            + " join products p on od.product_id=p.id"

                            + " where u.id=?1",
            nativeQuery = true)
    List<OrderDetailView> getAllOrderByUserId(Long userI);

    @Query(
            value =
                    "select o.orderId as orderId, o.status as status, u.username as username, u.email as email, u.first_name as firstName, u.last_name as lastName, u.address as uAddress, o.address as address,"
                            + " o.phone as phone, od.amount as amount, p.id as productId, p.name as name, p.price as price, p.remain as remain,"
                            + " (select top(1) i.url from image i where i.product_id=p.id) as url"
                            + " from users u left join orders o on u.id=o.user_id"
                            + " join orderdetails od on o.orderid=od.order_id"
                            + " join products p on od.product_id=p.id",

            nativeQuery = true)
    List<OrderDetailView> getAllOrder();

    @Override
    Optional<Order> findById(Long aLong);

    Optional<Order> findByOrderTime(Date date);


    List<Order> findByUser_Id(Long userId);
}
