package com.example.classhubproject.service.community;


import com.example.classhubproject.data.community.*;
import com.example.classhubproject.exception.ClassHubErrorCode;
import com.example.classhubproject.exception.ClassHubException;
import com.example.classhubproject.mapper.community.CommunityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CommunityService {

    private final CommunityMapper communityMapper;

    public void posting(CommunityRequestDTO communityRequestDTO) {

        // db에 게시글 정보 삽입
        int cnt = communityMapper.posting(communityRequestDTO);
        if (cnt == 1) {
            List<Integer> imageIds = communityRequestDTO.getCommunityImageIds();
            // 게시글 생성 후 키 반환
            Integer communityId = communityRequestDTO.getCommunityId();
            if (imageIds != null) {
                for (Integer imageId : imageIds) {
                    CommunityImageUploadRequestDTO communityImageUploadRequestDTO = new CommunityImageUploadRequestDTO(communityId, imageId);
                    communityMapper.insertCommunityToImage(communityImageUploadRequestDTO);
                }
            }
        } else {
            throw new ClassHubException(ClassHubErrorCode.POST_COMMUNITY_ERROR);
        }
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
                    String folder = "/home/ubuntu/contents/images";
                    file.transferTo(new File(folder + "/" + newFileName));
                    CommunityImageRequestDTO communityImageRequestDTO = new CommunityImageRequestDTO(0, folder + "/" + newFileName);

                    //db에 관련 정보 저장
                    Integer imageId = communityMapper.insertImage(communityImageRequestDTO);
                    imageIds.add(communityImageRequestDTO.getCommunityImageId());
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
        CommunityResponseDTO test = communityMapper.selectQuestion(id);
        List<String> images = test.getImage();
        List<String> imageForFront = new ArrayList<>();
        for (String image : images) {
            String imagePath = "https://api.devproject.store" + image;
            imageForFront.add(imagePath);
        }
        CommunityResponseDTO result = CommunityResponseDTO.builder()
                .userId(test.getUserId())
                .nickname(test.getNickname())
                .communityId(test.getCommunityId())
                .communityType(test.getCommunityType())
                .title(test.getTitle())
                .text(test.getText())
                .regDate(test.getRegDate())
                .editDate(test.getEditDate())
                .favoriteCount(test.getFavoriteCount())
                .commentCount(test.getCommentCount())
                .imageIds(test.getImageIds())
                .image(imageForFront)
                .likeUsers(test.getLikeUsers())
                .build();

        return result;
    }

    public CommunityResponseDTO selectStudy(Integer id) {
        CommunityResponseDTO test = communityMapper.selectStudy(id);
        List<String> images = test.getImage();
        List<String> imageForFront = new ArrayList<>();
        for (String image : images) {
            String imagePath = "https://api.devproject.store" + image;
            imageForFront.add(imagePath);
        }
        CommunityResponseDTO result = CommunityResponseDTO.builder()
                .userId(test.getUserId())
                .nickname(test.getNickname())
                .communityId(test.getCommunityId())
                .communityType(test.getCommunityType())
                .title(test.getTitle())
                .text(test.getText())
                .regDate(test.getRegDate())
                .editDate(test.getEditDate())
                .favoriteCount(test.getFavoriteCount())
                .commentCount(test.getCommentCount())
                .imageIds(test.getImageIds())
                .image(imageForFront)
                .likeUsers(test.getLikeUsers())
                .build();

        return result;
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

    public void modifyBoard(Integer communityId, CommunityRequestDTO communityRequestDTO) {
        int cnt = communityMapper.updateBoard(communityId, communityRequestDTO);
        if (cnt == 1) {
            List<Integer> imageIds = communityRequestDTO.getCommunityImageIds();
            if (imageIds != null) {
                for (Integer imageId : imageIds) {
                    CommunityImageUploadRequestDTO communityImageUploadRequestDTO = new CommunityImageUploadRequestDTO(communityId, imageId);
                    communityMapper.insertCommunityToImage(communityImageUploadRequestDTO);
                }
            }
        } else {
            throw new ClassHubException(ClassHubErrorCode.MODIFY_COMMUNITY_ERROR);
        }
    }

    public void deleteFiles(List<Integer> removeImageIds) {

        for (Integer removeImageId : removeImageIds) {
            if (removeImageId > 0) {
                // db에서 삭제할 이미지 이름 조회
                String fileName = communityMapper.selectImageNameById(removeImageId);
                File removeFile = new File(fileName);

                // ubuntu에서 이미지 삭제
                if (removeFile.exists()) {
                    boolean result = removeFile.delete();
                    //db에 관련 정보 삭제
                    communityMapper.removeImage(removeImageId);
                    communityMapper.removeImagePath(removeImageId);
                    log.info("파일이 삭제되었습니다 : " + fileName);
                } else {
                    log.info("파일이 존재하지 않습니다: " + fileName);
                    boolean result = false;
                }
            }
        }
    }

    public List<CommunityResponseDTO> selectCommunityForMainpage() {
        List<CommunityResponseDTO> list = communityMapper.selectCommunityForMainpage();
        List<CommunityResponseDTO> listForMain = new ArrayList<>();
        for (CommunityResponseDTO response : list) {
            CommunityResponseDTO result = CommunityResponseDTO.builder()
                    .userId(response.getUserId())
                    .nickname(response.getNickname())
                    .communityId(response.getCommunityId())
                    .communityType(response.getCommunityType())
                    .title(response.getTitle())
                    .text(response.getText())
                    .regDate(response.getRegDate())
                    .editDate(response.getEditDate())
                    .favoriteCount(response.getFavoriteCount())
                    .commentCount(response.getCommentCount())
                    .imageIds(response.getImageIds())
                    .likeUsers(response.getLikeUsers())
                    .build();
            listForMain.add(result);
        }
        return listForMain;
    }
//    public List<CommunityResponseDTO> selectQuestionForMypage(Integer userId) {
//        List<CommunityResponseDTO> list = communityMapper.selectQuestionForMypage(userId);
//        List<CommunityResponseDTO> listForMypage = new ArrayList<>();
//        for (CommunityResponseDTO response : list) {
//            CommunityResponseDTO result = CommunityResponseDTO.builder()
//                    .userId(response.getUserId())
//                    .nickname(response.getNickname())
//                    .communityId(response.getCommunityId())
//                    .communityType(response.getCommunityType())
//                    .title(response.getTitle())
//                    .text(response.getText())
//                    .regDate(response.getRegDate())
//                    .editDate(response.getEditDate())
//                    .favoriteCount(response.getFavoriteCount())
//                    .commentCount(response.getCommentCount())
//                    .imageIds(response.getImageIds())
//                    .likeUsers(response.getLikeUsers())
//                    .build();
//            listForMypage.add(result);
//        }
//        return listForMypage;
//    }
//
//    public List<CommunityResponseDTO> selectStduyForMypage(Integer userId) {
//        List<CommunityResponseDTO> list = communityMapper.selectStudyForMypage(userId);
//        List<CommunityResponseDTO> listForMypage = new ArrayList<>();
//        for (CommunityResponseDTO response : list) {
//            CommunityResponseDTO result = CommunityResponseDTO.builder()
//                    .userId(response.getUserId())
//                    .nickname(response.getNickname())
//                    .communityId(response.getCommunityId())
//                    .communityType(response.getCommunityType())
//                    .title(response.getTitle())
//                    .text(response.getText())
//                    .regDate(response.getRegDate())
//                    .editDate(response.getEditDate())
//                    .favoriteCount(response.getFavoriteCount())
//                    .commentCount(response.getCommentCount())
//                    .imageIds(response.getImageIds())
//                    .likeUsers(response.getLikeUsers())
//                    .build();
//            listForMypage.add(result);
//        }
//        return listForMypage;
//    }

    public List<CommunityResponseDTO> selectMyCommunityByUserId(Integer userId) {
        List<CommunityResponseDTO> list = communityMapper.selectStudyForMypage(userId);
        List<CommunityResponseDTO> listForMypage = new ArrayList<>();
        for (CommunityResponseDTO response : list) {
            CommunityResponseDTO result = CommunityResponseDTO.builder()
                    .userId(response.getUserId())
                    .nickname(response.getNickname())
                    .communityId(response.getCommunityId())
                    .communityType(response.getCommunityType())
                    .title(response.getTitle())
                    .text(response.getText())
                    .regDate(response.getRegDate())
                    .editDate(response.getEditDate())
                    .favoriteCount(response.getFavoriteCount())
                    .commentCount(response.getCommentCount())
                    .imageIds(response.getImageIds())
                    .likeUsers(response.getLikeUsers())
                    .build();
            listForMypage.add(result);
            return communityMapper.selectCommunityForMyPage(userId);
        }
        return listForMypage;
    }
}