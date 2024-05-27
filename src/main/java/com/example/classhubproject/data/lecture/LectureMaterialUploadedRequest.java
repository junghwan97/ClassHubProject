package com.example.classhubproject.data.lecture;

import lombok.Data;

@Data
public class LectureMaterialUploadedRequest {
    Integer resourceId;
	Integer classId;
    String resource;
}
