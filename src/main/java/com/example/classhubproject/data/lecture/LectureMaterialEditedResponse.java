package com.example.classhubproject.data.lecture;

import lombok.Data;

@Data
public class LectureMaterialEditedResponse {
    Integer edited;

    public Integer getEdited() {
        return edited;
    }

    public void setEdited(Integer edited) {
        this.edited = edited;
    }
}
