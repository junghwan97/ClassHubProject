package com.example.classhubproject.data.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "장바구니 Request DTO")
public class CartRequestDTO {

    @Schema(description = "장바구니 고유 번호")
    private int cartId;

    @Schema(description = "회원 ID")
    private int userId;

    @Schema(description = "강의 ID")
    private int classId;

}
