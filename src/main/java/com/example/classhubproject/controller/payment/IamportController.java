package com.example.classhubproject.controller.payment;

import com.example.classhubproject.data.common.ResponseData;
import com.example.classhubproject.data.common.ResponseMessage;
import com.example.classhubproject.data.payment.PaymentResponseDTO;
import com.example.classhubproject.service.payment.IamportService;
import com.siot.IamportRestClient.response.IamportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "아임포트 인증 기능 모음", description = "아임포트 인증 관련 기능을 처리")
public class IamportController {

    @Autowired
    IamportService iamportService;

    // 토큰 발급받기
    @Operation(
            summary = "아임포트 토큰 받기",
            description = "아임포트 토큰을 받아옵니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "토큰 받기 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "500", description = "토큰 받기 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
            }
    )
    @PostMapping("/access-token")
    public ResponseEntity<ResponseData<String>> getAccessToken() {
        try {
            String accessToken = iamportService.getToken();
            ResponseData<String> response = ResponseData.res(HttpStatus.OK.value(), ResponseMessage.ACCESS_TOKEN_SUCCESS, accessToken);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseData<String> errorResponse = ResponseData.res(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseMessage.ACCESS_TOKEN_ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }



}
