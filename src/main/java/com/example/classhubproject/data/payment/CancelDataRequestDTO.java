package com.example.classhubproject.data.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "결제 취소 Request DTO")
public class CancelDataRequestDTO {

    @Schema(description = "아임포트 식별자")
    private String impUid;

}
