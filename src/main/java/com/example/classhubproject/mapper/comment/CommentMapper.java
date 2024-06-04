package com.example.classhubproject.mapper.comment;

import com.example.classhubproject.data.comment.CommentResponseDTO;
import com.example.classhubproject.data.comment.CommentRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<CommentResponseDTO> selectAllByCommunityId(Integer communityId);

    Integer commentPost(CommentRequestDTO commentRequestDTO);

    Integer commentDelete(int commentId);

    CommentResponseDTO selectById(int commentId);

    Integer commentModify(@Param("commentId") int commentId, @Param("commentModify") CommentRequestDTO commentRequestDTO);

//    CommentResponseDTO selectById(Integer commentId);
}
