package com.example.classhubproject.data.lecture;

import lombok.Data;

import java.sql.Date;

@Data
public class LectureClassDetailDTO {

	private Integer classDetailId;
	private Integer classId;
	private String sectionTitle;
	private String title;
	private String video;
	private Integer videoLength;
	private Date regdate;
	private Date editDate;
}
