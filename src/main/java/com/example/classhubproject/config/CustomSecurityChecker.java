package com.example.classhubproject.config;

import com.example.classhubproject.data.comment.CommentResponseDTO;
import com.example.classhubproject.data.community.CommunityResponseDTO;
import com.example.classhubproject.mapper.comment.CommentMapper;
import com.example.classhubproject.mapper.community.CommunityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomSecurityChecker {

    private final CommunityMapper communityMapper;

    private final CommentMapper commentMapper;

    public boolean checkCommunityWriter(Authentication authentication, Integer communityId) {
        CommunityResponseDTO community = communityMapper.selectById(communityId);

        String username = authentication.getName();
        String writer = community.getNickname();

        return username.equals(writer);
    }

    public boolean checkCommentWriter(Authentication authentication, Integer commentId) {
        CommentResponseDTO comment = commentMapper.selectById(commentId);

        return comment.getNickname().equals(authentication.getName());
    }
}
//@PreAuthorize("isAuthenticated() and @customSecurityChecker.checkCommunityWriter(authentication, #communityId)")
