package com.example.classhubproject.service.lecture;

import com.example.classhubproject.data.lecture.*;
import com.example.classhubproject.exception.NoDataFoundException;
import com.example.classhubproject.mapper.lecture.LectureMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    	String uploadFolder = "/home/ubuntu/lecture";
    	
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
    	
    	String uploadFolder = "/home/ubuntu/lecture";

		// /home/ubuntu/lecture
		//"C:\\Users\\USER\\Desktop\\dummy"
    	
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

	public int favoriteLecture(Integer classId) {
		int userId = getUserId();
		return lectureMapper.favoriteLecture(classId, userId);
	}

	public int clearFavoriteLecture(Integer classId) {
		int userId = getUserId();
		return lectureMapper.clearFavoriteLecture(classId, userId);
	}

	//테스트용 하드코딩
	public int getUserId(){
		return 1;
	}
    
    public List<ClassResponseDTO> recommendLectures(ClassResponseDTO request){
    	List<ClassResponseDTO> lectures = lectureMapper.selectAll();
    	
    	Map<ClassResponseDTO, Double> similarityScores = new HashMap<>();
    	
    	for(ClassResponseDTO dto : lectures) {
    		if(!dto.getClassName().equalsIgnoreCase(request.getClassName())) {
    			double similarity = calculateCosineSimilarity(request.getClassName(), dto.getClassName());
    			similarityScores.put(dto, similarity);
    		}
    	}
    	
    	return similarityScores.entrySet().stream()
    			.sorted(Map.Entry.<ClassResponseDTO, Double>comparingByValue().reversed())
    			.limit(5)
    			.map(entry -> entry.getKey())
    			.collect(Collectors.toList());
    }
    
    private double calculateCosineSimilarity(String text1, String text2) {
        Map<String, Integer> wordFreq1 = getWordFrequency(text1);
        Map<String, Integer> wordFreq2 = getWordFrequency(text2);

        Set<String> allWords = new HashSet<>();
        allWords.addAll(wordFreq1.keySet());
        allWords.addAll(wordFreq2.keySet());

        int dotProduct = 0;
        int norm1 = 0;
        int norm2 = 0;

        for (String word : allWords) {
            int freq1 = wordFreq1.getOrDefault(word, 0);
            int freq2 = wordFreq2.getOrDefault(word, 0);
            dotProduct += freq1 * freq2;
            norm1 += freq1 * freq1;
            norm2 += freq2 * freq2;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private Map<String, Integer> getWordFrequency(String text) {
        Map<String, Integer> wordFreq = new HashMap<>();
        String[] words = text.toLowerCase().split("\\W+");
        for (String word : words) {
            wordFreq.put(word, wordFreq.getOrDefault(word, 0) + 1);
        }
        return wordFreq;
    }



}
