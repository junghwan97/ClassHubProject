package com.example.classhubproject.service.lecture;

import com.example.classhubproject.data.lecture.LectureDto;
import com.example.classhubproject.data.lecture.LectureUploadedRequest;
import com.example.classhubproject.data.lecture.LectureUploadedResponse;
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

        //System.out.println(request.);
//        Map<String, Object> result = new HashMap<>();
//        result.put("favoriteCommunity", false);

        //int upload = lectureMapper.upload(request);
        LectureUploadedResponse result  = new LectureUploadedResponse();
        result.uploaded = true;

        return result;
    }

}
