package com.example.classhubproject.data.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주문 Response DTO")
public class OrderResponseDTO {


    @Schema(description = "주문 ID")
    private int ordersId;

    @Schema(description = "회원 ID")
    private int userId;

    @Schema(description = "주문명")
    private String orderName;

    @Schema(description = "총 주문 금액")
    private int totalPrice;

    @Schema(description = "최종 주문 상태")
    private int finalOrderStatus;

    @Schema(description = "주문 일자")
    private Date regdate;



}
