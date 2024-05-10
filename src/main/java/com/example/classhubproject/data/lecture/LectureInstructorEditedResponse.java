package com.example.classhubproject.data.lecture;

import lombok.Data;

@Data
public class LectureInstructorEditedResponse {
    public Integer getEdited() {
        return edited;
    }

    public void setEdited(Integer edited) {
        this.edited = edited;
    }

    Integer edited;
}
