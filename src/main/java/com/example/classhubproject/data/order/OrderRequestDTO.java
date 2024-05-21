package com.example.classhubproject.data.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주문 Request DTO")
public class OrderRequestDTO {

    @Schema(description = "주문 ID")
    private int ordersId;

    @Schema(description = "주문 상세 ID")
    private int orderDetailId;

    @Schema(description = "회원 ID")
    private int userId;

    @Schema(description = "강의 ID")
    private int classId;

    @Schema(description = "총 주문 금액")
    private int totalPrice;

}
