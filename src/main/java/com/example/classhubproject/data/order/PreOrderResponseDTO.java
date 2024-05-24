package com.example.classhubproject.data.order;

import com.example.classhubproject.data.lecture.ClassResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@NoArgsConstructor
@Schema(description = "임시 주문 목록 Response DTO")
public class PreOrderResponseDTO {

    @Schema(description = "장바구니 고유 번호")
    private int cartId;

    @Schema(description = "회원 ID")
    private int userId;

    @Schema(description = "강의 ID")
    private int classId;

    @Schema(description = "강의 정보", implementation = ClassResponseDTO.class)
    private ClassResponseDTO classResponseDTO;

}
