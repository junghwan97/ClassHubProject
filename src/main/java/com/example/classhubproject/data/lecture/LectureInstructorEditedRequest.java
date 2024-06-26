package com.example.classhubproject.data.lecture;

import lombok.Data;

@Data
public class LectureInstructorEditedRequest {
    Integer     instructorsId;
    Integer     userId;
    String      name;
    String      field;
    String      text;
    String   userType;
    String   requestStatus;
}
