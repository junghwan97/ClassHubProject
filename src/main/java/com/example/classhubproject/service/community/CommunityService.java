package com.example.classhubproject.service.community;


import com.example.classhubproject.data.community.CommunityDto;
import com.example.classhubproject.data.community.CommunityModifyDto;
import com.example.classhubproject.data.community.CommunityRegDto;
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

    public int posting(CommunityRegDto communityRegDto, MultipartFile[] files) throws Exception {
//    public int posting(CommunityRegDto communityRegDto) throws Exception {

        //게시물 insert
        int cnt = communityMapper.posting(communityRegDto);
        System.out.println(communityRegDto.getCommunityId());
        for (MultipartFile file : files) {
            if (file.getSize() > 0) {
                // 파일 저장
                // 폴더 만들기
                String folder = "C:\\ProjectLMS\\upload\\" + communityRegDto.getCommunityId();
                File targetFolder = new File(folder);
                if (!targetFolder.exists()) {
                    targetFolder.mkdirs();
                }

                String path = folder + "\\" + file.getOriginalFilename();
                File target = new File(path);
                file.transferTo(target);
                //db에 관련 정보 저장
                communityMapper.insertImage(communityRegDto.getCommunityId(), file.getOriginalFilename());
            }
        }


        return 0;
    }

    public List<CommunityDto> QuestionsList() {
        return communityMapper.selectAllQuestions();
    }

    public List<CommunityDto> StudiesList() {
        return communityMapper.selectAllStudies();
    }

    public CommunityDto selectQuestion(Integer id) {
        return communityMapper.selectQuestion(id);
    }

    public CommunityDto selectStudy(Integer id) {
        return communityMapper.selectStudy(id);
    }

    public Integer modifyCommunity(Integer communityId, CommunityModifyDto communityModifyDto) {
        System.out.println(communityModifyDto.getTitle());
        return communityMapper.modifyCommunity(communityId, communityModifyDto);
    }

}
