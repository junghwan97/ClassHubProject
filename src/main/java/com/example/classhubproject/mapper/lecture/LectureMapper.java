package com.example.classhubproject.mapper.lecture;

import com.example.classhubproject.data.lecture.LectureUploadedRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LectureMapper {
    int upload(LectureUploadedRequest request);
}
