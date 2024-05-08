package com.example.classhubproject.data.comment;

import lombok.Data;

import java.sql.Date;

@Data
public class CommentDto {
    Integer commentId;
    Integer communityId;
    String text;
    Integer userId;
    Date regdate;
    Date editDate;
}
