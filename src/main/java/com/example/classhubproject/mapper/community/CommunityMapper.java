package com.example.classhubproject.mapper.community;

import com.example.classhubproject.data.community.CommunityDto;
import com.example.classhubproject.data.community.CommunityModifyDto;
import com.example.classhubproject.data.community.CommunityRegDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommunityMapper {
    int posting(CommunityRegDto communityRegDto);
    Integer insertImage(Integer community_id, String image);
    List<CommunityDto> selectAllQuestions();

    List<CommunityDto> selectAllStudies();

    CommunityDto selectQuestion(Integer id);

    CommunityDto selectStudy(Integer id);

    Integer modifyCommunity(@Param("communityId") Integer communityId, @Param("communityModifyDto") CommunityModifyDto communityModifyDto);

}