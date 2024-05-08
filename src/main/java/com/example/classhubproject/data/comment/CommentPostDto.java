package com.example.classhubproject.data.comment;

import lombok.Data;

@Data
public class CommentPostDto {
    Integer user_id;
    String text;
    Integer community_id;
}
