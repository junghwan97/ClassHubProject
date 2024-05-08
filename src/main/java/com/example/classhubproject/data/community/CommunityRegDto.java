package com.example.classhubproject.data.community;

import lombok.Data;

@Data
public class CommunityRegDto {
    Integer userId;
    Integer communityId;
    Character communityType;
    String title;
    String text;
}
