package com.example.classhubproject.data.lecture;

import lombok.Data;
import java.sql.Date;

@Data
public class LectureClassUploadedRequest {
    Integer classId;
    Integer instructorsId;
    Integer categoryId;
    String className;
    String description;
    String summary;
    Integer price;
    String thumnail;
    Integer totalVideoLength;
    Date regdate;
    Date editDate;
}
