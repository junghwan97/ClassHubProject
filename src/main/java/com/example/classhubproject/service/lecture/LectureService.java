package com.example.classhubproject.service.lecture;

import com.example.classhubproject.data.community.PagingDTO;
import com.example.classhubproject.data.lecture.*;
import com.example.classhubproject.exception.ClassHubErrorCode;
import com.example.classhubproject.exception.ClassHubException;
import com.example.classhubproject.mapper.lecture.LectureMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
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

	public String deleteInstructor(Integer userId){
		if(lectureMapper.selectInstructor(userId) == 0){
			return "등록되지 않은 강사입니다.";
		}
		try {
			lectureMapper.deleteInstructor(userId);
			lectureMapper.turnbackUser(userId);
		} catch (Exception e) {
			return e.getMessage();
		}
		return userId + "강사 삭제 되었습니다.";

	}

    public int addInstructor(LectureInstructorAddedRequest request) {

        int upload = lectureMapper.addInstructor(request);
		lectureMapper.addUserRole(request.getUserId());
     
        return upload;
    }

    public int editInstructor(LectureInstructorEditedRequest request) {

        int edited = lectureMapper.editInstructor(request);

        return edited;
    }

    public int uploadAndSyncMaterials(Integer classId, List<MultipartFile> files) {
        String uploadFolder = "/home/ubuntu/contents/material";

		// /home/ubuntu/contents/material
		//"C:\\Users\\USER\\Desktop\\dummy\\material"
        File uploadPath = new File(uploadFolder, classId.toString());

        if (!uploadPath.exists()) {
			boolean isCreated = uploadPath.mkdirs();  // mkdirs() 사용하여 전체 경로 생성 시도
			if (!isCreated) {
				log.info("Failed to create directory: " + uploadPath);
			}
		}

        try {
            // 데이터베이스에서 기존 파일 목록을 가져옴
            List<LectureMaterialUploadedRequest> existingMaterials;
            try {
                existingMaterials = selectMaterial(classId);
            } catch (Exception e) {
                // 데이터가 없을 때 빈 목록을 사용
				existingMaterials = List.of();
            }

            Set<String> existingFileNames = existingMaterials.stream()
                                                             .map(LectureMaterialUploadedRequest::getResource)
                                                             .collect(Collectors.toSet());

            // 새 파일 목록의 파일 이름을 Set으로 저장
            Set<String> newFileNames = new HashSet<>();
            for (MultipartFile file : files) {
                newFileNames.add(file.getOriginalFilename());
            }

            // 로컬 파일을 순회하며, 새 파일 목록에 없는 파일을 삭제
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(uploadPath.toPath())) {
                for (Path localFile : stream) {
                    if (!newFileNames.contains(localFile.getFileName().toString())) {
                        Files.delete(localFile);
						lectureMapper.deleteMaterial(classId, localFile.getFileName().toString());
                        System.out.println("파일 삭제: " + localFile);
                    }
                }
            }

            // 새 파일을 업로드하거나 업데이트
            for (MultipartFile f : files) {
                String fileName = f.getOriginalFilename();
                if (!existingFileNames.contains(fileName)) {
                    // 데이터베이스에 새 파일 정보 삽입
                    LectureMaterialUploadedRequest request = new LectureMaterialUploadedRequest();
                    request.setClassId(classId);
                    request.setResource(fileName);
                    lectureMapper.uploadMaterial(request);
                }

                // 파일을 로컬에 저장
                File saveFile = new File(uploadPath, fileName);
                try {
                    f.transferTo(saveFile);
                    System.out.println("파일 업로드/업데이트: " + saveFile);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }


	public List<LectureMaterialUploadedRequest> selectMaterial(Integer classId){

		List<LectureMaterialUploadedRequest> res = lectureMapper.selectMaterial(classId);

		if(res.isEmpty()) {
			throw new ClassHubException(ClassHubErrorCode.NO_DATA_FOUND);
		}
		return res;
	}




	public LectureMaterialEditedResponse editMaterial(LectureMaterialEditedRequest request) {

        int edited = lectureMapper.editMaterial(request);

        LectureMaterialEditedResponse response  = new LectureMaterialEditedResponse();
        response.setEdited(edited);

        return response;
    }

    // 강의 추가/ 수정
	public int uploadAndSyncClass(LectureClassUploadedRequest request, String sectionsJson, List<MultipartFile> videos) throws IOException {
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

		try {
			// 해당 클래스 아이디로 강의가 이미 존재하는지 확인
			LectureClassUploadedRequest existingClass = lectureMapper.selectByIdForUpdate(request.getClassId());

			if(existingClass != null) {
				// 이미 존재하는 경우, 강의 정보를 업데이트
				lectureMapper.updateClass(request);
			} else {
				// 존재하지 않는 경우, 새로운 강의로 등록
				lectureMapper.uploadClass(request);
			}
		} catch (Exception e) {
			log.info("gggg" + e.getMessage());
			throw new RuntimeException(e);
		}

		String uploadFolder = "/home/ubuntu/contents/videos";
		SimpleDateFormat save = new SimpleDateFormat("yyyyMMddHHmmss");

		Date date = new Date();
		String str = String.valueOf(request.getClassId());
		String random = save.format(date);

		File uploadPath = new File(uploadFolder, str);

		if(uploadPath.exists() == false) {
			boolean isCreated = uploadPath.mkdirs();
			if (!isCreated) {
				log.error("Failed to create directory: " + uploadPath);
			}
		}

		log.info("1번");

		// 기존 파일 목록을 가져옴
		List<LectureClassDetailDTO> existingVideos;
		try {
			existingVideos = lectureMapper.selectClassDetail(request.getClassId());
		} catch (Exception e) {
			existingVideos = List.of();
		}

		Set<String> existingFileNames = existingVideos.stream()
				.map(LectureClassDetailDTO::getVideo)
				.collect(Collectors.toSet());

		Set<String> newFileNames = new HashSet<>();
		for (SectionDTO sections : dto.getSections()) {
			for (LectureClassDetailDTO video : sections.getVideos()) {
				newFileNames.add(random + "_" + video.getVideo());
			}
		}

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(uploadPath.toPath())) {
			for (Path localFile : stream) {
				if (!newFileNames.contains(localFile.getFileName().toString())) {
					Files.delete(localFile);
					// localFile.getFileName().toString()은 이미 앞에 random 값과 "_"가 포함되어 있음
					lectureMapper.deleteClassDetail(request.getClassId(), localFile.getFileName().toString());
					System.out.println("파일 삭제: " + localFile);
				}
			}
		}

		for (SectionDTO sections : dto.getSections()) {
			for (LectureClassDetailDTO video : sections.getVideos()) {
				video.setClassId(request.getClassId());
				video.setSectionTitle(sections.getSectionTitle());
				video.setVideo(random + "_" + video.getVideo());
				if (!existingFileNames.contains(video.getVideo())) {
					lectureMapper.addClassVideo(video);
				}
			}
		}

		log.info("2번");

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
		return request.getClassId();
	}

    public LectureClassEditedResponse editClass(LectureClassEditedRequest request) {

        int edited = lectureMapper.editClass(request);

        LectureClassEditedResponse response  = new LectureClassEditedResponse();
        response.setEdited(edited);

        return response;
    }

	public PagingDTO<List<ClassResponseDTO>> selectAll(Integer page){
		int rowPerPage = 6;

		// 쿼리 LIMIT절에 사용할 시작 인덱스
		int startIndex = (page - 1) * rowPerPage;
		List<ClassResponseDTO> list1 = lectureMapper.selectAll(0, 10000);
		List<ClassResponseDTO> list = lectureMapper.selectAll(startIndex, rowPerPage);
		int numOfRecord = list1.size();
		System.out.println(numOfRecord + "adsfdsafdsafdasf");
		PagingDTO pgList = createPaging(page, numOfRecord, list);


    	return pgList;
    }

	public PagingDTO<List<ClassResponseDTO>> selectByKeyword(String keyword, Integer page){

		int rowPerPage = 6;

		// 쿼리 LIMIT절에 사용할 시작 인덱스
		int startIndex = (page - 1) * rowPerPage;
		List<ClassResponseDTO> list1 = lectureMapper.selectByKeyword(keyword, 0, 10000);
		List<ClassResponseDTO> list = lectureMapper.selectByKeyword(keyword, startIndex, rowPerPage);
		int numOfRecord = list1.size();
		System.out.println(numOfRecord + "adsfdsafdsafdasf");
		PagingDTO pgList = createPaging(page, numOfRecord, list);

		return pgList;

    }
    
    public PagingDTO<List<ClassResponseDTO>> selectByCategory(Integer categoryId, Integer page){
		int rowPerPage = 6;

		// 쿼리 LIMIT절에 사용할 시작 인덱스
		int startIndex = (page - 1) * rowPerPage;
		List<ClassResponseDTO> list1 = lectureMapper.selectByCategory(categoryId, 0, 10000);
		List<ClassResponseDTO> list = lectureMapper.selectByCategory(categoryId, startIndex, rowPerPage);
		int numOfRecord = list1.size();
		System.out.println(numOfRecord + "adsfdsafdsafdasf");
		PagingDTO pgList = createPaging(page, numOfRecord, list);

		return pgList;
    }

	public List<ClassResponseDTO> findClassByUserId(Integer userId){
		return lectureMapper.findClassByUserId(userId);
	}


	public PagingDTO createPaging(Integer page, Integer numOfRecords, List<ClassResponseDTO> list) {
		// 한페이지 당 게시물 수
		Integer rowPerPage = 6;

		// 쿼리 LIMIT절에 사용할 시작 인덱스
		int startIndex = (page - 1) * rowPerPage;

		// 페이지네이션에 필요한 정보
		// 마지막 페이지 번호
		int totalNum = (numOfRecords - 1) / rowPerPage + 1;
		System.out.println(totalNum);
		//페이지네이션 왼쪽 번호
		int leftEndNum = page - 5;
		leftEndNum = Math.max(leftEndNum, 1);
		System.out.println(leftEndNum);
		//페이지네이션 오른쪽 번호
		int rightEndNum = leftEndNum + 9;
		System.out.println(rightEndNum);
		rightEndNum = Math.min(rightEndNum, totalNum);
		int currentPageNum = page;

		PagingDTO pagingList = new PagingDTO(list, currentPageNum, totalNum, leftEndNum, rightEndNum);
		return pagingList;
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
    
//    public List<ClassResponseDTO> recommendLectures(ClassResponseDTO request){
//    	List<ClassResponseDTO> lectures = lectureMapper.selectAll();
//
//    	Map<ClassResponseDTO, Double> similarityScores = new HashMap<>();
//
//    	for(ClassResponseDTO dto : lectures) {
//    		if(!dto.getClassName().equalsIgnoreCase(request.getClassName())) {
//    			double similarity = calculateCosineSimilarity(request.getClassName(), dto.getClassName());
//    			similarityScores.put(dto, similarity);
//    		}
//    	}
//
//    	return similarityScores.entrySet().stream()
//    			.sorted(Map.Entry.<ClassResponseDTO, Double>comparingByValue().reversed())
//    			.limit(5)
//    			.map(entry -> entry.getKey())
//    			.collect(Collectors.toList());
//    }
//
//    private double calculateCosineSimilarity(String text1, String text2) {
//        Map<String, Integer> wordFreq1 = getWordFrequency(text1);
//        Map<String, Integer> wordFreq2 = getWordFrequency(text2);
//
//        Set<String> allWords = new HashSet<>();
//        allWords.addAll(wordFreq1.keySet());
//        allWords.addAll(wordFreq2.keySet());
//
//        int dotProduct = 0;
//        int norm1 = 0;
//        int norm2 = 0;
//
//        for (String word : allWords) {
//            int freq1 = wordFreq1.getOrDefault(word, 0);
//            int freq2 = wordFreq2.getOrDefault(word, 0);
//            dotProduct += freq1 * freq2;
//            norm1 += freq1 * freq1;
//            norm2 += freq2 * freq2;
//        }
//
//        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
//    }

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
			dto.setVideo("https://api.devproject.store/home/ubunt/contents/videos/" + classId + "/" + dto.getVideo());

			group.computeIfAbsent(dto.getSectionTitle(), k -> new ArrayList<>()).add(dto);
		}
		List<List<LectureClassDetailDTO>> response = new ArrayList<>(group.values());
		return response;
	}

	public void learningPoint(LearningDataDTO request) {
		int userId = getUserId();

		if(request.getVideoEndTime() == null){							//러닝데이터 초기 등록
			lectureMapper.insertLearningPoint(request);
		}else{															//러닝데이터 종료시간 수정 및 진도율 계산
			//넘겨줄때 총 시간 같이 넘겨줄수있는지
			lectureMapper.updateLearningPoint(request);
		}
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

	public ClassDetailResponseDTO responseForUpdateVideo(Integer classId){
		List<String> sectionTitles = lectureMapper.selectSectionTitle(classId);
		List<LectureClassDetailDTO> classDetails = lectureMapper.selectClassDetail(classId);
		String className = lectureMapper.getClassInfoByClassId(classId).getClassName();

		ClassDetailResponseDTO response = new ClassDetailResponseDTO();
		List<SectionDTO> sectionDtos = new ArrayList<>();
		response.setTitle(className);

		for(String sectionTitle : sectionTitles){
			SectionDTO dto = new SectionDTO();
			dto.setSectionTitle(sectionTitle);
			List<LectureClassDetailDTO> sectionDetails = new ArrayList<>();

			for(LectureClassDetailDTO classDetail : classDetails){
				if(classDetail.getSectionTitle().equals(sectionTitle)){
					sectionDetails.add(classDetail);
				}
			}
			dto.setVideos(sectionDetails);
			sectionDtos.add(dto);
		}
		response.setSections(sectionDtos);

		return response;
	}


}

