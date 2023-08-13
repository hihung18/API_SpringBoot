package com.example.api_java.controller.impl;

import com.example.api_java.controller.IBaseController;
import com.example.api_java.controller.IGetController;
import com.example.api_java.model.dto.EventDTO;
import com.example.api_java.service.impl.EventServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import lombok.Getter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@CrossOrigin("*")
@RestController
@RequestMapping("api/event")
@Tag(name = "Event")
public class EventControllerImpl implements IBaseController<EventDTO, Long, EventServiceImpl>
        , IGetController<EventDTO, Long, EventServiceImpl> {
        @Resource
        @Getter
        private EventServiceImpl service;
}
