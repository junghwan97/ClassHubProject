package com.example.classhubproject.service.lecture;

import com.example.classhubproject.data.lecture.*;
import com.example.classhubproject.mapper.lecture.LectureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class LectureService {
    private final LectureMapper lectureMapper;

    @Autowired
    public LectureService(LectureMapper lectureMapper){
        this.lectureMapper = lectureMapper;
    }

    public LectureUploadedResponse upload(LectureUploadedRequest request) {

        int upload = lectureMapper.upload(request);

        LectureUploadedResponse response  = new LectureUploadedResponse();
        response.setUploaded(upload);

        return response;
    }

    public LectureEditedResponse edit(LectureEditedRequest request) {

        int edited = lectureMapper.edit(request);

        LectureEditedResponse response  = new LectureEditedResponse();
        response.setEdited(edited);

        return response;
    }

}
