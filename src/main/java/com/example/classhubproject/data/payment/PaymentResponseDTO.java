package com.example.classhubproject.data.payment;

import com.example.classhubproject.data.order.OrderResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "결제 Response DTO")
public class PaymentResponseDTO {

    @Schema(description = "결제 ID")
    private int paymentId;

    @Schema(description = "주문 ID")
    private int ordersId;

    @Schema(description = "포트원 결제 고유번호")
    private String impUid;

    @Schema(description = "가맹점 주문번호")
    private String merchantUid;

    @Schema(description = "결제 대행사")
    private String pgProvider;

    @Schema(description = "결제 수단")
    private String payMethod;

    @Schema(description = "결제 금액")
    private BigDecimal paymentAmount;

    @Schema(description = "결제 상태")
    private String paymentStatus;

    @Schema(description = "결제일시")
    private Date paidAt;

    @Schema(description = "결제 취소일시")
    private Date cancelledAt;

    @Schema(description = "주문 정보", implementation = OrderResponseDTO.class)
    private OrderResponseDTO orderResponseDTO;
    
    

}
