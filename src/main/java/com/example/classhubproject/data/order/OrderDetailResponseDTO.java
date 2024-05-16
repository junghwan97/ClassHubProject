package com.example.classhubproject.data.order;

import com.example.classhubproject.data.lecture.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주문 상세 Response DTO")
public class OrderDetailResponseDTO {

    @Schema(description = "주문 상세 ID")
    private int orderDetailId;

    @Schema(description = "주문 ID")
    private int ordersId;

    @Schema(description = "강의 ID")
    private int classId;

    @Schema(description = "강의 정보", implementation = ClassResponseDTO.class)
    private ClassResponseDTO classResponseDTO;


}
