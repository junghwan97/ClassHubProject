package com.example.classhubproject.data.lecture;

import lombok.Data;

@Data
public class LectureUploadedResponse {
    public Integer getUploaded() {
        return uploaded;
    }

    public void setUploaded(Integer uploaded) {
        this.uploaded = uploaded;
    }

    Integer uploaded;
}
