package com.example.classhubproject.mapper.lecture;

import com.example.classhubproject.data.lecture.LectureInstructorEditedRequest;
import com.example.classhubproject.data.lecture.LectureMaterialEditedRequest;
import com.example.classhubproject.data.lecture.LectureMaterialUploadedRequest;
import com.example.classhubproject.data.lecture.LectureInstructorAddedRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LectureMapper {
    // 강의
    int upload(LectureInstructorAddedRequest request);
    int edit(LectureInstructorEditedRequest request);
    // 강의 자료
    int uploadMaterial(LectureMaterialUploadedRequest request);
    int editMaterial(LectureMaterialEditedRequest request);
}
