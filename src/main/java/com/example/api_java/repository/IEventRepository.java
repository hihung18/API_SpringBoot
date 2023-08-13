package com.example.api_java.repository;


import com.example.api_java.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IEventRepository extends JpaRepository<Event, Long> {
    @Override
    Optional<Event> findById(Long along);
}
