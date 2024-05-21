package com.example.classhubproject.mapper.lecture;

import com.example.classhubproject.data.lecture.*;

import java.util.List;

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

    // 강의 가격
    int getClassPrice(int classId);
    
    //강의 영상등록
    int addClassVideo(LectureClassDetailDTO dto);
    
    //강의 전체조회
    List<ClassResponseDTO> selectAll();

    //강의 키워드 조회
    List<ClassResponseDTO> selectByKeyword(String keyword);
    
    //카데고리 조회
    List<ClassResponseDTO> selectByCategory(Integer categoryId);

}
