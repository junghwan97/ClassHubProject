package com.example.classhubproject.mapper.sample;

import com.example.classhubproject.data.sample.SampleDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SampleMapper {

    List<SampleDto> getAll();
}
