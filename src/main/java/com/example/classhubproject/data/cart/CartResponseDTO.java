package com.example.classhubproject.data.cart;

import com.example.classhubproject.data.lecture.ClassResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@NoArgsConstructor
@Schema(description = "장바구니 Response DTO")
public class CartResponseDTO {

    @Schema(description = "장바구니 고유 번호")
    private int cartId;

    @Schema(description = "회원 ID")
    private int userId;

    @Schema(description = "강의 ID")
    private int classId;

    @Schema(description = "장바구니 등록일자", defaultValue = "현재 날짜와 시간", format = "date-time")
    private Date regdate;

    @Schema(description = "주문 완료 여부", defaultValue = "2", allowableValues = {"1", "2"})
    private String orderStatus;

    @Schema(description = "강의 정보", implementation = ClassResponseDTO.class)
    private ClassResponseDTO classResponseDTO;

}
