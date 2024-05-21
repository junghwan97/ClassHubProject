package com.example.classhubproject.data.lecture;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class LectureClassDetailDTO {

	private Integer class_id;
	private String title;
	private String video;
	private Integer video_length;
	private Date regdate;
	private Date edit_date;
}
