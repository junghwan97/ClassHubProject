package com.example.classhubproject.service.community;


import com.example.classhubproject.data.community.CommunityResponseDTO;
import com.example.classhubproject.data.community.CommunityRequestDTO;
import com.example.classhubproject.mapper.community.CommunityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CommunityService {

    private final CommunityMapper communityMapper;

    @Autowired
    public CommunityService(CommunityMapper communityMapper) {
        this.communityMapper = communityMapper;
    }

    public int posting(CommunityRequestDTO communityRequestDTO, MultipartFile[] files) {
        int cnt = communityMapper.posting(communityRequestDTO);
        try {
            //게시물 insert
            for (MultipartFile file : files) {
                if (file.getSize() > 0) {
                    // 파일 저장
                    // 폴더 만들기(로컬 환경)
                    String folder = "C:\\Users\\jhzza\\Desktop\\ProjectLMS\\upload\\" + communityRequestDTO.getCommunityId();
                    File targetFolder = new File(folder);
                    if (!targetFolder.exists()) {
                        targetFolder.mkdirs();
                    }

                    String path = folder + "\\" + file.getOriginalFilename();
                    File target = new File(path);
                    file.transferTo(target);
                    //db에 관련 정보 저장
                    communityMapper.insertImage(communityRequestDTO.getCommunityId(), file.getOriginalFilename());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cnt;
    }

    public List<CommunityResponseDTO> questionsRecentList() {

        return communityMapper.selectAllRecentQuestions();
    }

    public List<CommunityResponseDTO> questionsFavoriteList() {
        return communityMapper.selectAllQuestionsByFavorite();
    }

    public List<CommunityResponseDTO> questionsCommentList() {
        return communityMapper.selectAllQuestionsByComment();
    }

    public List<CommunityResponseDTO> studiesRecentList() {
        return communityMapper.selectAllRecentStudies();
    }
    public List<CommunityResponseDTO> studiesFavoriteList() {
        return communityMapper.selectAllStudiesByFavorite();
    }

    public List<CommunityResponseDTO> studiesCommentList() {
        return communityMapper.selectAllStudiesByComment();
    }

    public CommunityResponseDTO selectQuestion(Integer id) {
        return communityMapper.selectQuestion(id);
    }

    public CommunityResponseDTO selectStudy(Integer id) {
        return communityMapper.selectStudy(id);
    }

    public Integer modifyCommunity(Integer communityId, CommunityRequestDTO communityDto, MultipartFile[] addFiles, List<String> removeFiles) {
        try {
            // Image 테이블 삭제
            if (removeFiles != null && !removeFiles.isEmpty()) {
                for (String fileName : removeFiles) {
                    // 하드웨어에서 삭제
                    String path = "C:\\Users\\jhzza\\Desktop\\ProjectLMS\\upload\\" + communityId + "\\" + fileName;
                    File file = new File(path);
                    if (file.exists()) {
                        file.delete();
                    }
                    // 테이블에서 삭제
                    communityMapper.removeImage(communityId, fileName);
                }
            }
            // 새 파일 추가
            if (addFiles != null) {


                for (MultipartFile newFile : addFiles) {
                    if (newFile.getSize() > 0) {
                        // image 테이블에 파일명 추가
                        communityMapper.insertImage(communityId, newFile.getOriginalFilename());

                        String fileName = newFile.getOriginalFilename();
                        String folder = "C:\\Users\\jhzza\\Desktop\\ProjectLMS\\upload\\" + communityId;
                        String path = folder + "\\" + fileName;

                        // 디렉토리 없으면 만듦
                        File dir = new File(folder);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }

                        // 하드디스크에 파일 저장
                        File file = new File(path);
                        newFile.transferTo(file);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 게시물 테이블 수정
        int cnt = communityMapper.modifyCommunity(communityId, communityDto);

        return cnt;
    }

    public List<CommunityResponseDTO> studiesStatusList(int status) {
        return communityMapper.selectStudiesByStatus(status);
    }
}
