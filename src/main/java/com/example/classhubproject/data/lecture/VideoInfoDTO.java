package com.example.classhubproject.data.lecture;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VideoInfoDTO {

	private String videoTitle;
	private Integer videoLength;
	private MultipartFile video;
	
	public VideoInfoDTO(List<SectionDTO> sections) {
		
	}
}
