package com.example.classhubproject.data.lecture;

import lombok.Data;

@Data
public class LectureUploadedRequest {
    public Integer     user_id;
    public String      name;
    public String      field;
    public String      text;
    public String   user_type;
    public String   request_status;
}
