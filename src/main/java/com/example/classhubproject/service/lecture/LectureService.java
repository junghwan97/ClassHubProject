package com.example.classhubproject.service.lecture;

import com.example.classhubproject.data.lecture.*;
import com.example.classhubproject.mapper.lecture.LectureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class LectureService {
    private final LectureMapper lectureMapper;

    @Autowired
    public LectureService(LectureMapper lectureMapper){
        this.lectureMapper = lectureMapper;
    }

    public LectureInstructorAddedResponse addInstructor(LectureInstructorAddedRequest request) {

        int upload = lectureMapper.addInstructor(request);

        LectureInstructorAddedResponse response  = new LectureInstructorAddedResponse();
        response.setUploaded(upload);

        return response;
    }

    public LectureInstructorEditedResponse editInstructor(LectureInstructorEditedRequest request) {

        int edited = lectureMapper.editInstructor(request);

        LectureInstructorEditedResponse response  = new LectureInstructorEditedResponse();
        response.setEdited(edited);

        return response;
    }

    public LectureMaterialUploadedResponse uploadMaterial(LectureMaterialUploadedRequest request) {

        int upload = lectureMapper.uploadMaterial(request);

        LectureMaterialUploadedResponse response  = new LectureMaterialUploadedResponse();
        response.setUpload(upload);

        return response;
    }

    public LectureMaterialEditedResponse editMaterial(LectureMaterialEditedRequest request) {

        int edited = lectureMapper.editMaterial(request);

        LectureMaterialEditedResponse response  = new LectureMaterialEditedResponse();
        response.setEdited(edited);

        return response;
    }

    // 강의 추가/ 수정
    public LectureClassUploadedResponse uploadClass(LectureClassUploadedRequest request) {

        int upload = 0;//;lectureMapper.uploadClass(request);

        LectureClassUploadedResponse response  = new LectureClassUploadedResponse();
        response.setUpload(upload);

        return response;
    }

    public LectureClassEditedResponse editClass(LectureClassEditedRequest request) {

        int edited = 0;//lectureMapper.editClass(request);

        LectureClassEditedResponse response  = new LectureClassEditedResponse();
        response.setEdited(edited);

        return response;
    }

}
