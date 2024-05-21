package com.example.classhubproject.data.lecture;

import lombok.Data;

@Data
public class LectureMaterialUploadedResponse {
    Integer upload;

    public Integer getUpload() {
        return upload;
    }

    public void setUpload(Integer upload) {
        this.upload = upload;
    }
}
