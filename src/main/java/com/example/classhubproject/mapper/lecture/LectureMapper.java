package com.example.classhubproject.mapper.lecture;

import com.example.classhubproject.data.lecture.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LectureMapper {
    // 강사 추가/수정
    int addInstructor(LectureInstructorAddedRequest request);
    int editInstructor(LectureInstructorEditedRequest request);
    // 강의 자료 추가/수정
    int uploadMaterial(LectureMaterialUploadedRequest request);
    int editMaterial(LectureMaterialEditedRequest request);

    // 강의 추가/수정
    int uploadClass(LectureClassUploadedRequest request);
    int editClass(LectureClassEditedRequest request);
}
