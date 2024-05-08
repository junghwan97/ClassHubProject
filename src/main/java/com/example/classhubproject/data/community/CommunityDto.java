package com.example.classhubproject.data.community;

import lombok.Data;

import java.sql.Date;

@Data
public class CommunityDto {
    Integer communityId;
    Integer userId;
    Character communityType;
    String title;
    String text;
    Date regDate;
    Date editDate;
    Integer favoriteCount;
    Integer commentCount;
}
