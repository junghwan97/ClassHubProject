package com.example.classhubproject.mapper.comment;

import com.example.classhubproject.data.comment.CommentDto;
import com.example.classhubproject.data.comment.CommentModifyDto;
import com.example.classhubproject.data.comment.CommentPostDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<CommentDto> selectAllByCommunityId(Integer communityId);

    Integer commentPost(CommentPostDto commentPostDto);

    Integer commentDelete(int commentId);

    CommentDto getCommentById(int commentId);

    Integer commentModify(@Param("commentId") int commentId, @Param("commentModifyDto") CommentModifyDto commentModifyDto);
}
