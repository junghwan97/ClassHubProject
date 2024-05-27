package com.example.classhubproject.service.lecture;

import com.example.classhubproject.data.lecture.*;
import com.example.classhubproject.exception.NoDataFoundException;
import com.example.classhubproject.mapper.lecture.LectureMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public int uploadMaterial(Integer id, List<MultipartFile> files) {

    	String uploadFolder = "C:\\Users\\USER\\Desktop\\dummy";
    	
    	File uploadPath = new File(uploadFolder, id.toString());
    	
    	if(uploadPath.exists() == false) {
    		uploadPath.mkdir();
    	}
    	
    	try {
			for(MultipartFile f : files) {
				LectureMaterialUploadedRequest request = new LectureMaterialUploadedRequest();
				request.setClassId(id);
				request.setResource(f.getOriginalFilename());
				
				lectureMapper.uploadMaterial(request);
				
				File saveFile = new File(uploadPath, f.getOriginalFilename());
				
				try {
					f.transferTo(saveFile);
				}catch(Exception e) {
					log.error(e.getMessage());
				}
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
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
    	
    	//폴더 생성 및 업로드 Date정보로 머릿글 생성 
    	
    	String uploadFolder = "C:\\Users\\USER\\Desktop\\dummy";
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat save = new SimpleDateFormat("yyyyMMddHHmmss");
    	
    	Date date = new Date();
    	
    	String str = sdf.format(date);
    	
    	String random = save.format(date);
    	
    	File uploadPath = new File(uploadFolder, str);
  
    	if(uploadPath.exists() == false) {
    		uploadPath.mkdir();
    	}
    	
    	//파일 정보 DB업로드
    	for(LectureClassDetailDTO video : sections.getVideos()) {
    		    		
    		video.setClass_id(request.getClass_id());
    		
    		video.setVideo(random + "_" + video.getVideo());
    		lectureMapper.addClassVideo(video);	
    	}
    	
    	//파일 업로드
    	for(MultipartFile file : videos) {
    		String temp = random + "_" + file.getOriginalFilename();
    		File saveFile = new File(uploadPath, temp);
    		
    		try {
    			file.transferTo(saveFile);
    		}catch(Exception e) {
    			log.error(e.getMessage());
    		}
    	}

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
    
    public List<LectureMaterialUploadedRequest> selectMaterial(Integer classId){
    	
    	List<LectureMaterialUploadedRequest> res = lectureMapper.selectMaterial(classId);

    	if(res.isEmpty()) {
    		throw new NoDataFoundException("조회 데이터 없음");
    	}
			return res;		
    }

}
