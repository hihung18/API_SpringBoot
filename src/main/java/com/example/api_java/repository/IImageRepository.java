package com.example.api_java.repository;

import com.example.api_java.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByProductProductId(Long productId);

    @Modifying
    @Query(value = "DELETE image where product_id=?1", nativeQuery = true)
    void deleteAllByProductId(Long productId);
}
