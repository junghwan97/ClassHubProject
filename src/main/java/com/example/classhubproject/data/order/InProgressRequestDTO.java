package com.example.classhubproject.data.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "임시 주문 생성 Request DTO")
public class InProgressRequestDTO {

    @Schema(description = "회원 ID")
    private int userId;

    @Schema(description = "강의 ID 리스트", example ="[1, 2, 3]")
    private List<Integer> classIds;

}
