package com.example.classhubproject.data.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Schema(description = "결제 금액 사전 등록 Request DTO")
public class PaymentPrepareRequestDTO {

    @Schema(description = "가맹점 주문 번호")
    private String merchantUid;
    
    @Schema(description = "사전 등록한 결제 예정 금액")
    private BigDecimal amount;

}
