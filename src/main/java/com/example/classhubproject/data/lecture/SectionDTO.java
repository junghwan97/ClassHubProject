package com.example.classhubproject.data.lecture;

import java.util.List;

import lombok.Data;

@Data
public class SectionDTO {

	private String title;
	private List<LectureClassDetailDTO> videos;
	
}
