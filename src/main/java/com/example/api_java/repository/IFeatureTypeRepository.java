package com.example.api_java.repository;

import com.example.api_java.model.entity.FeatureType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface IFeatureTypeRepository extends JpaRepository<FeatureType, Long> {
    @Override
    @NotNull
    Optional<FeatureType> findById(Long s);

    @Override
    <S extends FeatureType> S save(S entity);

    @Override
    void delete(FeatureType entity);
}
