package com.example.classhubproject.data.lecture;

import lombok.Data;

@Data
public class LectureUploadedRequest {
    Integer     user_id;
    String      name;
    String      field;
    String      text;
    String   user_type;
    String   request_status;
}