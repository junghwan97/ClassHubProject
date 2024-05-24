package com.example.classhubproject.data.payment;

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
@Schema(description = "결제 금액 사전 등록 Response DTO")
public class PaymentPrepareResponseDTO {

    @Schema(description = "응답코드 0이면 정상")
    private int code;

    @Schema(description = "code 값이 0이 아닐 때의 오류 메세지")
    private String message;

    @Schema(description = "가맹점 주문 번호")
    private String merchantUid;

    @Schema(description = "사전 등록한 결제 예정 금액")
    private BigDecimal amount;

}
