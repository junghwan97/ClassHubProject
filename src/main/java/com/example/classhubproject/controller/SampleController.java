package com.example.classhubproject.controller;

import com.example.classhubproject.data.SampleDto;
import com.example.classhubproject.mapper.SampleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SampleController {

    private final SampleMapper mapper;

    @Autowired
    public SampleController(SampleMapper mapper) {
        this.mapper = mapper;
    }

    @GetMapping("selectAll")
    public List<SampleDto> getAll() {
        return mapper.getAll();
    }
}
