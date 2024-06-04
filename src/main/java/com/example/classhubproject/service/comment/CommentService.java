package com.example.classhubproject.service.comment;

import com.example.classhubproject.data.comment.CommentResponseDTO;
import com.example.classhubproject.data.comment.CommentRequestDTO;
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

    public List<CommentResponseDTO> commentList(Integer communityId) {
        return commentMapper.selectAllByCommunityId(communityId);
    }

    public Integer commentPost(CommentRequestDTO commentRequestDTO) {
        return commentMapper.commentPost(commentRequestDTO);
    }

    public Integer commentDelete(int commentId) {
        return commentMapper.commentDelete(commentId);
    }

//    public CommentResponseDTO getComment(int commentId) {
//        return commentMapper.getCommentById(commentId);
//    }

    public Integer commentModify(int commentId, CommentRequestDTO commentRequestDTO) {
        return commentMapper.commentModify(commentId, commentRequestDTO);
    }
}