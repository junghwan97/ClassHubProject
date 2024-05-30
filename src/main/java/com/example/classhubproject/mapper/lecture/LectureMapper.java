package com.example.classhubproject.mapper.lecture;

import com.example.classhubproject.data.lecture.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LectureMapper {
    // 강사 추가/수정
    int addInstructor(LectureInstructorAddedRequest request);

    int editInstructor(LectureInstructorEditedRequest request);

    // 강의 자료 추가/수정
    int uploadMaterial(LectureMaterialUploadedRequest request);

    List<LectureMaterialUploadedRequest> selectMaterial(Integer classId);

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

    ClassResponseDTO getClassInfoByClassId(int classId);

    int favoriteLecture(Integer classId, int userId);

    int clearFavoriteLecture(Integer classId, int userId);

    List<LectureClassDetailDTO> selectClassDetail(Integer classId);

    void learningPoint(LearningDataDTO request);

    ClassResponseDTO selectById(Integer classId);

    LearningDataDTO selectLearningData(Integer classDetailId, int userId);

    List<LearningDataDTO> selectAllLearningData(Integer classId, int userId);
}
