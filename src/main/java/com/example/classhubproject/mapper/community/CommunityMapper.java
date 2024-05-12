package com.example.classhubproject.mapper.community;

import com.example.classhubproject.data.community.CommunityResponseDTO;
import com.example.classhubproject.data.community.CommunityRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommunityMapper {
    int posting(CommunityRequestDTO communityRequestDTO);

    Integer insertImage(@Param("communityId") Integer communityId, @Param("image") String image);

    List<CommunityResponseDTO> selectAllRecentQuestions();

    List<CommunityResponseDTO> selectAllQuestionsByFavorite();

    List<CommunityResponseDTO> selectAllQuestionsByComment();

    List<CommunityResponseDTO> selectAllRecentStudies();
    List<CommunityResponseDTO> selectAllStudiesByFavorite();

    List<CommunityResponseDTO> selectAllStudiesByComment();

    CommunityResponseDTO selectQuestion(Integer id);

    CommunityResponseDTO selectStudy(Integer id);

    Integer modifyCommunity(@Param("communityId") Integer communityId, @Param("communityDto") CommunityRequestDTO communityDto);

    void removeImage(@Param("communityId") Integer communityId, @Param("fileName") String fileName);

    List<CommunityResponseDTO> selectStudiesByStatus(int status);

    Integer countAll();
}