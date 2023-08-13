package com.example.api_java.service;

import com.example.api_java.model.dto.UserDetailDTO;
import com.example.api_java.model.entity.UserDetail;

import java.util.List;

public interface IBaseService<D, ID> {
    List<D> findAll();

    D findById(ID id);

    D update(ID id, D d);

    D save(D d);

    D delete(ID id);

    UserDetailDTO createFromE(UserDetail entity);

    UserDetail updateEntity(UserDetail entity, UserDetailDTO dto);
}

