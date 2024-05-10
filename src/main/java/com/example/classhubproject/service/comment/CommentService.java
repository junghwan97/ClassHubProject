package com.example.classhubproject.service.comment;

import com.example.classhubproject.data.comment.CommentDto;
import com.example.classhubproject.data.comment.CommentModifyDto;
import com.example.classhubproject.data.comment.CommentPostDto;
import com.example.classhubproject.mapper.comment.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CommentService {

    private final CommentMapper commentMapper;

    @Autowired
    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public List<CommentDto> commentList(Integer communityId) {
        return commentMapper.selectAllByCommunityId(communityId);
    }

    public Integer commentPost(CommentPostDto commentPostDto) {
        return commentMapper.commentPost(commentPostDto);
    }

    public Integer commentDelete(int commentId) {
        return commentMapper.commentDelete(commentId);
    }

    public CommentDto getComment(int commentId) {
        return commentMapper.getCommentById(commentId);
    }

    public Integer commentModify(int commentId, CommentModifyDto commentModifyDto) {
        System.out.println(commentModifyDto.getText());
        return commentMapper.commentModify(commentId, commentModifyDto);
    }
}