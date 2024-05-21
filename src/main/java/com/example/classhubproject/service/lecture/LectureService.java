package com.example.classhubproject.service.lecture;

import com.example.classhubproject.data.lecture.*;
import com.example.classhubproject.mapper.lecture.LectureMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class LectureService {
    private final LectureMapper lectureMapper;

    @Autowired
    public LectureService(LectureMapper lectureMapper){
        this.lectureMapper = lectureMapper;
    }

    public int addInstructor(LectureInstructorAddedRequest request) {

        int upload = lectureMapper.addInstructor(request);
     
        return upload;
    }

    public int editInstructor(LectureInstructorEditedRequest request) {

        int edited = lectureMapper.editInstructor(request);

        return edited;
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
    public int uploadClass(LectureClassUploadedRequest request, String sectionsJson, List<MultipartFile> videos) throws JsonMappingException, JsonProcessingException {
    	
    	ObjectMapper obm = new ObjectMapper();
    	SectionDTO sections = obm.readValue(sectionsJson, SectionDTO.class);
    	Integer length = 0;
    	
    	//전체 길이 등록
    	for(LectureClassDetailDTO video : sections.getVideos()) {   		
    		length = length + video.getVideo_length();
    	}
    	request.setClass_name(sections.getTitle());
    	request.setTotal_video_length(length);
    	    	
    	int upload = lectureMapper.uploadClass(request);
    	
    	for(LectureClassDetailDTO video : sections.getVideos()) {
    		video.setClass_id(request.getClass_id());
    		lectureMapper.addClassVideo(video);
    	}
    	/*파일 스토리지 업로드 로직 추가 해야함 */
  
    	return upload;
    	
    	//파일따로, 정보만 따로 -->json배열안에 파일을 넣을수없음
    }

    public LectureClassEditedResponse editClass(LectureClassEditedRequest request) {

        int edited = lectureMapper.editClass(request);

        LectureClassEditedResponse response  = new LectureClassEditedResponse();
        response.setEdited(edited);

        return response;
    }
    
    public List<ClassResponseDTO> selectAll(){
    	
    	return lectureMapper.selectAll();
    }
    
    public List<ClassResponseDTO> selectByKeyword(String keyword){
    	
    	return lectureMapper.selectByKeyword(keyword);
    }
    
    public List<ClassResponseDTO> selectByCategory(Integer categoryId){
    	
    	return lectureMapper.selectByCategory(categoryId);
    } 

}
