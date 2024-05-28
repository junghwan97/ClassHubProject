package com.example.classhubproject.service.lecture;

import com.example.classhubproject.data.lecture.*;
import com.example.classhubproject.exception.NoDataFoundException;
import com.example.classhubproject.mapper.lecture.LectureMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	private final GroupedOpenApi lecture;

	@Autowired
    public LectureService(LectureMapper lectureMapper, @Qualifier("lecture") GroupedOpenApi lecture){
        this.lectureMapper = lectureMapper;
		this.lecture = lecture;
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

		String uploadFolder = "/home/ubuntu/contents/videos";
    	
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

		Integer length = 0;
		ClassDetailResponseDTO dto = obm.readValue(sectionsJson, ClassDetailResponseDTO.class);
		System.out.println(dto.toString());

		for(SectionDTO sections : dto.getSections()){
			for(LectureClassDetailDTO video : sections.getVideos()){
				length = length + video.getVideoLength();
			}
			request.setClassName(dto.getTitle());
			request.setTotalVideoLength(length);
		}
		System.out.println(request.toString());
		log.info("111111");
		int upload = 0;
		try {
			upload = lectureMapper.uploadClass(request);
		} catch (Exception e) {
			log.info("gggg" + e.getMessage());
			throw new RuntimeException(e);
		}
		log.info("ddddd");
    	//폴더 생성 및 업로드 Date정보로 머릿글 생성

    	String uploadFolder = "/home/ubuntu/contents/videos";

		// /home/ubuntu/contents/videos
		//"C:\\Users\\USER\\Desktop\\dummy"

    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat save = new SimpleDateFormat("yyyyMMddHHmmss");

    	Date date = new Date();

    	String str = String.valueOf(request.getClassId());

    	String random = save.format(date);

    	File uploadPath = new File(uploadFolder, str);

    	if(uploadPath.exists() == false) {
    		uploadPath.mkdir();
    	}
    	log.info("1번");
    	//파일 정보 DB업로드
		for(SectionDTO sections : dto.getSections()){
    		for(LectureClassDetailDTO video : sections.getVideos()) {

				video.setClassId(request.getClassId());
				video.setSectionTitle(sections.getSectionTitle());
				video.setVideo(random + "_" + video.getVideo());
				lectureMapper.addClassVideo(video);
			}
    	}
    	log.info("2번");
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
		log.info("3번");
    	return upload;
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


	public List<List<LectureClassDetailDTO>> selectClassDetail(Integer classId) {
		List<LectureClassDetailDTO> list = lectureMapper.selectClassDetail(classId);
		Map<String, List<LectureClassDetailDTO>> group = new LinkedHashMap<>();

		for(LectureClassDetailDTO dto : list){
			dto.setVideo("https://devproject.store/home/ubunt/contents/videos/" + classId + "/" + dto.getVideo());

			group.computeIfAbsent(dto.getSectionTitle(), k -> new ArrayList<>()).add(dto);
		}
		List<List<LectureClassDetailDTO>> response = new ArrayList<>(group.values());
		return response;
	}

	public void learningPoint(LearningDataDTO request) {
		lectureMapper.learningPoint(request);
	}

	public Map<String, Object> selectById(Integer classId) {
		Map<String, Object> response = new LinkedHashMap<>();

		// classInfo 정보 추가
		ClassResponseDTO classInfo = lectureMapper.selectById(classId);
		response.put("classInfo", classInfo);

		// classDetail 정보 추가
		List<List<LectureClassDetailDTO>> classDetail = selectClassDetail(classId);
		response.put("classDetail", classDetail);

		// learningData 정보 추가
		List<LearningDataDTO> learningData = selectAllLearningData(classId);
		response.put("learningData", learningData);

		// 총 진행률, 학습시간 계산 후 추가
		double percent = 0;
		int learningTime = 0;
		int count = 0;
		for(LearningDataDTO dto : learningData){
			percent = dto.getProgressRate() + percent;
			learningTime = dto.getVideoEndTime() + learningTime;
			count++;
		}
		double percentage = percent / count;

		response.put("learningTime", learningTime);
		response.put("percentage", Math.round(percentage));

		return response;
	}

	public LearningDataDTO selectLearningData(Integer classDetailId) {
		//원래 세션이나 다른걸로 받아와야함
		int userId = getUserId();
		return lectureMapper.selectLearningData(classDetailId, userId);
	}

	public List<LearningDataDTO> selectAllLearningData(Integer classId) {
		int userId = getUserId();
		return lectureMapper.selectAllLearningData(classId, userId);
	}
}
