package com.example.api_java.service.impl;

import com.example.api_java.exception.NotFoundException;
import com.example.api_java.model.dto.EventDTO;
import com.example.api_java.model.dto.UserDetailDTO;
import com.example.api_java.model.entity.Event;
import com.example.api_java.model.entity.UserDetail;
import com.example.api_java.repository.IEventRepository;
import com.example.api_java.service.IBaseService;
import com.example.api_java.service.IModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements IBaseService<EventDTO, Long>, IModelMapper<EventDTO, Event> {
    private final IEventRepository repository;
    private final ModelMapper modelMapper;

    public EventServiceImpl(IEventRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<EventDTO> findAll() {
        return createFromEntities(repository.findAll());
    }

    @Override
    public EventDTO findById(Long id) {
        Optional<Event> event = repository.findById(id);
        event.orElseThrow(() -> new NotFoundException(Event.class, id));
        return createFromE(event.get());
    }

    @Override
    public EventDTO update(Long aLong, EventDTO eventDTO) {
        return null;
    }

    @Override
    public EventDTO save(EventDTO eventDTO) {
        return null;
    }

    @Override
    public EventDTO delete(Long aLong) {
        return null;
    }

    @Override
    public UserDetailDTO createFromE(UserDetail entity) {
        return null;
    }
    @Override
    public UserDetail updateEntity(UserDetail entity, UserDetailDTO dto) {
        return null;
    }

    @Override
    public Event createFromD(EventDTO dto) {
        return null;
    }

    @Override
    public EventDTO createFromE(Event entity) {
        EventDTO dto = modelMapper.map(entity, EventDTO.class);
        return dto;
    }

    @Override
    public Event updateEntity(Event entity, EventDTO dto) {
        return null;
    }
}
