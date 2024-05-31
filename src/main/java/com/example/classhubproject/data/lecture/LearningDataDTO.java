package com.example.classhubproject.data.lecture;

import lombok.Data;

import java.util.Date;

@Data
public class LearningDataDTO {

    private Integer learningId;
    private Integer userId;
    private Integer classDetailId;
    private Integer videoEndTime;
    private Integer progressRate;
    private char completionStatus;
    private Date startDate;
    private Date endDate;

    public LearningDataDTO(Integer userId, Integer classDetailId){
        this.userId = userId;
        this.classDetailId = classDetailId;
    }
}
