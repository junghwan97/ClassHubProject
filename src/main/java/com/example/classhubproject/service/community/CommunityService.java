package com.example.classhubproject.service.community;


import com.example.classhubproject.data.community.*;
import com.example.classhubproject.mapper.community.CommunityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CommunityService {

    private final CommunityMapper communityMapper;

    @Autowired
    public CommunityService(CommunityMapper communityMapper) {
        this.communityMapper = communityMapper;
    }

    public int posting(CommunityRequestDTO communityRequestDTO) {

        List<Integer> imageIds = communityRequestDTO.getCommunityImageIds();
        Integer result = communityMapper.posting(communityRequestDTO);
        Integer communityId = communityRequestDTO.getCommunityId();
        if (imageIds != null) {
            for (Integer imageId : imageIds) {
                CommunityImageUploadRequestDTO communityImageUploadRequestDTO = new CommunityImageUploadRequestDTO(communityId, imageId);
                Integer result1 = communityMapper.insertCommunityToImage(communityImageUploadRequestDTO);
            }
        }
        return communityId;
    }


    public List<Integer> postingImage(List<MultipartFile> files) {

        List<Integer> imageIds = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                if (file != null) {
                    // 파일 저장
                    // ubuntu에 이미지 저장
                    String originalFilename = file.getOriginalFilename();
                    String newFileName = generateUniqueFileName(originalFilename);
                    String folder = "/home/ubuntu/images";
                    file.transferTo(new File(folder + "/" + newFileName));

                    CommunityImageRequestDTO communityImageRequestDTO = new CommunityImageRequestDTO(0, folder + "/" + newFileName);


                    //db에 관련 정보 저장
                    Integer imageId = communityMapper.insertImage(communityImageRequestDTO);
                    imageIds.add(communityImageRequestDTO.getCommunityImageId());
//                    communityMapper.insertPath(imageId, filePath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageIds;
    }

    private String generateUniqueFileName(String originalFileName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        //Random 객체 생성
        Random random = new Random();
        // 0이상 100 미만의 랜덤한 정수 반환
        String randomNumber = Integer.toString(random.nextInt(Integer.MAX_VALUE));
        String timeStamp = dateFormat.format(new Date());
        return timeStamp + randomNumber + originalFileName;
    }

    public PagingDTO<List<CommunityResponseDTO>> questionsRecentList(Integer page, String search, String type) {
        // 한페이지 당 게시물 수
        Integer rowPerPage = 5;

        // 쿼리 LIMIT절에 사용할 시작 인덱스
        int startIndex = (page - 1) * rowPerPage;

        // 페이지네이션에 필요한 정보
        // 전체 레코드 수
        int numOfRecords = communityMapper.countAllQuestions(search, type);
        // 마지막 페이지 번호
        int lastPageNum = (numOfRecords - 1) / rowPerPage + 1;
        //페이지네이션 왼쪽 번호
        int leftPageNum = page - 5;
        leftPageNum = Math.max(leftPageNum, 1);
        //페이지네이션 오른쪽 번호
        int rightPageNum = leftPageNum + 9;
        rightPageNum = Math.min(rightPageNum, lastPageNum);
        int currentPageNum = page;

        // 게시물 목록
        List<CommunityResponseDTO> list = communityMapper.selectAllRecentQuestions(startIndex, rowPerPage, search, type);
        PagingDTO pagingList = new PagingDTO(list, currentPageNum, lastPageNum, leftPageNum, rightPageNum);
        return pagingList;
    }

    public PagingDTO<List<CommunityResponseDTO>> questionsFavoriteList(Integer page, String search, String type) {
        // 한페이지 당 게시물 수
        Integer rowPerPage = 5;

        // 쿼리 LIMIT절에 사용할 시작 인덱스
        int startIndex = (page - 1) * rowPerPage;

        // 페이지네이션에 필요한 정보
        // 전체 레코드 수
        int numOfRecords = communityMapper.countAllQuestions(search, type);
        // 마지막 페이지 번호
        int lastPageNum = (numOfRecords - 1) / rowPerPage + 1;
        //페이지네이션 왼쪽 번호
        int leftPageNum = page - 5;
        leftPageNum = Math.max(leftPageNum, 1);
        //페이지네이션 오른쪽 번호
        int rightPageNum = leftPageNum + 9;
        rightPageNum = Math.min(rightPageNum, lastPageNum);
        int currentPageNum = page;

        List<CommunityResponseDTO> list = communityMapper.selectAllQuestionsByFavorite(startIndex, rowPerPage, search, type);
        PagingDTO pagingList = new PagingDTO(list, currentPageNum, lastPageNum, leftPageNum, rightPageNum);
        return pagingList;
    }

    public PagingDTO<List<CommunityResponseDTO>> questionsCommentList(Integer page, String search, String type) {
        // 한페이지 당 게시물 수
        Integer rowPerPage = 5;

        // 쿼리 LIMIT절에 사용할 시작 인덱스
        int startIndex = (page - 1) * rowPerPage;

        // 페이지네이션에 필요한 정보
        // 전체 레코드 수
        int numOfRecords = communityMapper.countAllQuestions(search, type);
        // 마지막 페이지 번호
        int lastPageNum = (numOfRecords - 1) / rowPerPage + 1;
        //페이지네이션 왼쪽 번호
        int leftPageNum = page - 5;
        leftPageNum = Math.max(leftPageNum, 1);
        //페이지네이션 오른쪽 번호
        int rightPageNum = leftPageNum + 9;
        rightPageNum = Math.min(rightPageNum, lastPageNum);
        int currentPageNum = page;

        List<CommunityResponseDTO> list = communityMapper.selectAllQuestionsByComment(startIndex, rowPerPage, search, type);
        PagingDTO pagingList = new PagingDTO(list, currentPageNum, lastPageNum, leftPageNum, rightPageNum);
        return pagingList;
    }

    public PagingDTO<List<CommunityResponseDTO>> studiesRecentList(Integer page, String search, String type) {
        // 한페이지 당 게시물 수
        Integer rowPerPage = 5;

        // 쿼리 LIMIT절에 사용할 시작 인덱스
        int startIndex = (page - 1) * rowPerPage;

        // 페이지네이션에 필요한 정보
        // 전체 레코드 수
        int numOfRecords = communityMapper.countAllStudies(search, type);
        // 마지막 페이지 번호
        int lastPageNum = (numOfRecords - 1) / rowPerPage + 1;
        //페이지네이션 왼쪽 번호
        int leftPageNum = page - 5;
        leftPageNum = Math.max(leftPageNum, 1);
        //페이지네이션 오른쪽 번호
        int rightPageNum = leftPageNum + 9;
        rightPageNum = Math.min(rightPageNum, lastPageNum);
        int currentPageNum = page;

        List<CommunityResponseDTO> list = communityMapper.selectAllRecentStudies(startIndex, rowPerPage, search, type);
        PagingDTO pagingList = new PagingDTO(list, currentPageNum, lastPageNum, leftPageNum, rightPageNum);
        return pagingList;
    }

    public PagingDTO<List<CommunityResponseDTO>> studiesFavoriteList(Integer page, String search, String type) {
        // 한페이지 당 게시물 수
        Integer rowPerPage = 5;

        // 쿼리 LIMIT절에 사용할 시작 인덱스
        int startIndex = (page - 1) * rowPerPage;

        // 페이지네이션에 필요한 정보
        // 전체 레코드 수
        int numOfRecords = communityMapper.countAllStudies(search, type);
        // 마지막 페이지 번호
        int lastPageNum = (numOfRecords - 1) / rowPerPage + 1;
        //페이지네이션 왼쪽 번호
        int leftPageNum = page - 5;
        leftPageNum = Math.max(leftPageNum, 1);
        //페이지네이션 오른쪽 번호
        int rightPageNum = leftPageNum + 9;
        rightPageNum = Math.min(rightPageNum, lastPageNum);
        int currentPageNum = page;

        List<CommunityResponseDTO> list = communityMapper.selectAllStudiesByFavorite(startIndex, rowPerPage, search, type);
        PagingDTO pagingList = new PagingDTO(list, currentPageNum, lastPageNum, leftPageNum, rightPageNum);
        return pagingList;
    }

    public PagingDTO<List<CommunityResponseDTO>> studiesCommentList(Integer page, String search, String type) {
        // 한페이지 당 게시물 수
        Integer rowPerPage = 5;

        // 쿼리 LIMIT절에 사용할 시작 인덱스
        int startIndex = (page - 1) * rowPerPage;

        // 페이지네이션에 필요한 정보
        // 전체 레코드 수
        int numOfRecords = communityMapper.countAllStudies(search, type);
        // 마지막 페이지 번호
        int totalNum = (numOfRecords - 1) / rowPerPage + 1;
        //페이지네이션 왼쪽 번호
        int leftEndNum = page - 5;
        leftEndNum = Math.max(leftEndNum, 1);
        //페이지네이션 오른쪽 번호
        int rightEndNum = leftEndNum + 9;
        rightEndNum = Math.min(rightEndNum, totalNum);
        int currentPageNum = page;

        List<CommunityResponseDTO> list = communityMapper.selectAllStudiesByComment(startIndex, rowPerPage, search, type);
        PagingDTO pagingList = new PagingDTO(list, currentPageNum, totalNum, leftEndNum, rightEndNum);
        return pagingList;
    }

    public CommunityResponseDTO selectQuestion(Integer id) {
        return communityMapper.selectQuestion(id);
    }

    public CommunityResponseDTO selectStudy(Integer id) {
        return communityMapper.selectStudy(id);
    }

    public PagingDTO<List<CommunityResponseDTO>> studiesStatusList(int status, int page, String search, String type) {
        // 한페이지 당 게시물 수
        Integer rowPerPage = 5;

        // 쿼리 LIMIT절에 사용할 시작 인덱스
        int startIndex = (page - 1) * rowPerPage;

        // 페이지네이션에 필요한 정보
        // 전체 레코드 수
        int numOfRecords = communityMapper.countAllStudiesByStatus(status, search, type);
        // 마지막 페이지 번호
        int totalNum = (numOfRecords - 1) / rowPerPage + 1;
        //페이지네이션 왼쪽 번호
        int leftEndNum = page - 5;
        leftEndNum = Math.max(leftEndNum, 1);
        //페이지네이션 오른쪽 번호
        int rightEndNum = leftEndNum + 9;
        rightEndNum = Math.min(rightEndNum, totalNum);
        int currentPageNum = page;

//        List<CommunityResponseDTO> list = communityMapper.selectAllStudiesByComment(startIndex, rowPerPage, search, type);
        List<CommunityResponseDTO> list = communityMapper.selectStudiesByStatus(status, startIndex, rowPerPage, search, type);
        PagingDTO pagingList = new PagingDTO(list, currentPageNum, totalNum, leftEndNum, rightEndNum);
        return pagingList;

    }

    public Integer modifyBoard(Integer communityId, CommunityRequestDTO communityRequestDTO) {
        return communityMapper.updateBoard(communityId, communityRequestDTO);
    }

    public Integer deleteFiles(List<Integer> removeImageIds) {

        int cnt = 0;
        for (Integer removeImageId : removeImageIds) {
            if (removeImageId > 0) {
                // 파일 저장
                // ubuntu에서 이미지 삭제
                String folder = "/home/ubuntu/images";
                String fileName = communityMapper.selectImageNameById(removeImageId);
//                String originalFilename = folder + "/" + fileName;
                File removeFile = new File(fileName);

                if (removeFile.exists()) {
                    boolean result = removeFile.delete();
                    //db에 관련 정보 삭제
                    cnt = communityMapper.removeImage(removeImageId);
                } else {
                    System.out.println("파일이 존재하지 않습니다: " + fileName);
                    log.info("파일이 존재하지 않습니다: " + fileName);
                    boolean result = false;
                }

            }
        }
        return cnt;
    }

    public Integer modifyFiles(List<Integer> communityImageId, List<MultipartFile> files) {
//        List<Integer> imageModifyIds = new ArrayList<>();
        int cnt = 0;
        try {
//
            for (int i = 0; i < communityImageId.size(); i++) {
                int imageId = communityImageId.get(i);
                MultipartFile file = files.get(i);
                // ubuntu에 이미지 저장
                String originalFilename = file.getOriginalFilename();
                String newFileName = generateUniqueFileName(originalFilename);
                String folder = "/home/ubuntu/images";
                String path = folder + "/" + newFileName;
                file.transferTo(new File(path));
                cnt += communityMapper.updateImage(imageId, path);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cnt;
    }
}