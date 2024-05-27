package com.example.classhubproject.mapper.community;

import com.example.classhubproject.data.community.CommunityImageRequestDTO;
import com.example.classhubproject.data.community.CommunityImageUploadRequestDTO;
import com.example.classhubproject.data.community.CommunityRequestDTO;
import com.example.classhubproject.data.community.CommunityResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommunityMapper {
    Integer posting(CommunityRequestDTO communityRequestDTO);

    Integer insertImage(CommunityImageRequestDTO communityImageRequestDTO);

    List<CommunityResponseDTO> selectAllRecentQuestions(@Param("startIndex")Integer startIndex,
                                                        @Param("rowPerPage") Integer rowPerPage,
                                                        @Param("search") String search,
                                                        @Param(("type")) String type);

    List<CommunityResponseDTO> selectAllQuestionsByFavorite(@Param("startIndex")Integer startIndex,
                                                            @Param("rowPerPage") Integer rowPerPage,
                                                            @Param("search") String search,
                                                            @Param(("type")) String type);

    List<CommunityResponseDTO> selectAllQuestionsByComment(@Param("startIndex")Integer startIndex,
                                                           @Param("rowPerPage") Integer rowPerPage,
                                                           @Param("search") String search,
                                                           @Param(("type")) String type);

    List<CommunityResponseDTO> selectAllRecentStudies(@Param("startIndex")Integer startIndex,
                                                      @Param("rowPerPage") Integer rowPerPage,
                                                      @Param("search") String search,
                                                      @Param(("type")) String type);
    List<CommunityResponseDTO> selectAllStudiesByFavorite(@Param("startIndex")Integer startIndex,
                                                          @Param("rowPerPage") Integer rowPerPage,
                                                          @Param("search") String search,
                                                          @Param(("type")) String type);

    List<CommunityResponseDTO> selectAllStudiesByComment(@Param("startIndex")Integer startIndex,
                                                         @Param("rowPerPage") Integer rowPerPage,
                                                         @Param("search") String search,
                                                         @Param(("type")) String type);

    CommunityResponseDTO selectQuestion(Integer id);

    CommunityResponseDTO selectStudy(Integer id);

    Integer modifyCommunity(@Param("communityId") Integer communityId, @Param("communityDto") CommunityRequestDTO communityDto);

    Integer removeImage(@Param("removeImageId") Integer removeImageId);

    List<CommunityResponseDTO> selectStudiesByStatus(@Param("status") int status,
                                                     @Param("startIndex")Integer startIndex,
                                                     @Param("rowPerPage") Integer rowPerPage,
                                                     @Param("search") String search,
                                                     @Param(("type")) String type);

    Integer countAllQuestions(@Param("search") String search, @Param("type") String type);

    Integer countAllStudies(@Param("search") String search, @Param("type") String type);

    Integer countAllStudiesByStatus(@Param("status") int status, @Param("search") String search, @Param("type") String type);

    Integer insertCommunityToImage(CommunityImageUploadRequestDTO communityImageUploadRequestDTO);

    Integer updateBoard(@Param("communityId") Integer communityId, @Param("communityRequest") CommunityRequestDTO communityRequestDTO);

    String selectImageNameById(Integer removeImageId);

    Integer updateImage(@Param("imageId") int imageId, @Param("fileName") String originalFilename);

    void removeImagePath(Integer removeImageId);
}