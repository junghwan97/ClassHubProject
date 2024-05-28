package com.example.classhubproject.data.lecture;

import lombok.Data;

import java.util.List;

@Data
public class ClassDetailResponseDTO {

    private String title;
    private List<SectionDTO> sections;
}
