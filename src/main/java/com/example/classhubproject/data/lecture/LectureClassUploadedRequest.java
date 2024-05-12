package com.example.classhubproject.data.lecture;

import lombok.Data;
import java.sql.Date;

@Data
public class LectureClassUploadedRequest {
    Integer class_id;
    Integer instructors_id;
    Integer category_id;
    String class_name;
    String description;
    String summary;
    Integer price;
    String video;
    Integer total_video_length;
    Date regdate;
    Date edit_date;
}
