package com.example.api_java.repository;

import com.example.api_java.model.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserDetailRepository extends JpaRepository<UserDetail, Long> {
    Optional<UserDetail> findByUsername(String username);

    Optional<UserDetail> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query(
            value =
                    "SELECT u.* from users u join rates r on u.id = r.user_id"
                            + " where r.product_id = :productId",
            nativeQuery = true)
    List<UserDetail> findAllByProductId(@Param(value = "productId") Long productId);

}
