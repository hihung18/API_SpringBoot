package com.example.api_java.repository;

import com.example.api_java.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCategoryCateId(Long cateId);

    List<Product> findByProductNameContaining(String productName);

    @Query(value = "SELECT * from products p where  p.id in (SELECT fd.product_id from feature_detail fd where fd.feature_id in ?1) and p.category_id = ?2", nativeQuery = true)
    List<Product> findAllByFilter(Set<Long> featureIds, Long cateId);

    @Query(value = "SELECT * from products p where  p.id in (SELECT fd.product_id from feature_detail fd where fd.feature_id in ?1)", nativeQuery = true)
    List<Product> findAllByFeaturesId(Set<Long> featureIds);

    @Query(value = "SELECT * from products p where  p.id in ?1", nativeQuery = true)
    List<Product> findAllByProductId(Set<Long> featureIds);

    List<Product> findAllByEvent_EventId(Long eventId);
}
