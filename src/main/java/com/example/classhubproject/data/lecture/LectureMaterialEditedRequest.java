package com.example.classhubproject.data.lecture;

import lombok.Data;

@Data
public class LectureMaterialEditedRequest {
    Integer resource_id;
    Integer class_detail_id;
    String resource;
}
