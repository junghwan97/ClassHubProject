package com.example.classhubproject.data.lecture;

import lombok.Data;

import java.util.List;

@Data
public class SectionDTO {

	private String sectiontitle;
	private List<LectureClassDetailDTO> videos;
	
}
