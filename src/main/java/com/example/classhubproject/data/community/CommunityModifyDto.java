package com.example.classhubproject.data.community;

import lombok.Data;

@Data
public class CommunityModifyDto {
    Integer userId;
    Character communityType;
    String title;
    String text;
}
