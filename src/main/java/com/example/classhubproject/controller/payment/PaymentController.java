package com.example.classhubproject.controller.payment;

import com.example.classhubproject.data.payment.*;
import com.example.classhubproject.service.payment.IamportService;
import com.example.classhubproject.service.payment.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/payments")
@Tag(name = "결제 기능 모음", description = "결제 관련 기능을 처리")
public class PaymentController implements InitializingBean {

    private final PaymentService paymentService;
    private final IamportService iamportService;

    @Value("${iamport.key}")
    private String apiKey;
    @Value("${iamport.secret}")
    private String secretKey;
    private IamportClient iamportClient;

    @Override
    public void afterPropertiesSet() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    // 사후 검증 후 결제 정보 저장
    @Operation(
            summary = "결제 정보 저장하기",
            description = "아임포트 식별자로 사후 검증 후 결제 정보를 저장합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "결제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDTO.class))),
                    @ApiResponse(responseCode = "409", description = "결제 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDTO.class)))
            }
    )
    @PostMapping("/add")
    public void addPayment(@RequestBody @Schema(description = "아임포트 식별자", example = "{imp_uid: string}") String impUid) {
        paymentService.addPayment(impUid);
    }


    // 결제 정보 가져오기
    @Operation(
            summary = "포트원 결제 정보 조회하기(단건)",
            description = "아임포트 식별자로 포트원 측 결제 정보를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "포트원 결제 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Payment.class))),
                    @ApiResponse(responseCode = "500", description = "포트원 결제 정보 조회 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Payment.class)))
            }
    )
    @GetMapping("/{imp_uid}")
    public IamportResponse<Payment> getPaymentByImpUid(@PathVariable("imp_uid") String imp_uid) {
        return iamportService.paymentByImpUid(imp_uid);
    }


    // 사전 검증 : 사전 결제 금액 등록
    @Operation(
            summary = "사전 결제 금액 등록하기",
            description = "가맹점 주문번호와 금액을 결제 전에 포트원 데이터베이스에 저장합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사전 결제 금액 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Prepare.class))),
                    @ApiResponse(responseCode = "500", description = "사전 결제 금액 등록 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Prepare.class)))
            }
    )
    @PostMapping("/prepare")
    public void preparePayment(@RequestBody PaymentPrepareRequestDTO request) {
        iamportService.postPrepare(request.getMerchantUid(), request.getAmount());
    }


    // 사전 결제 금액 값 가져오기
    @Operation(
            summary = "사전 결제 금액 조회",
            description = "사전 결제 금액을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "사전 결제 금액 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentPrepareResponseDTO.class)))
            }
    )
    @GetMapping("/prepare/{merchant_uid}")
    public PaymentPrepareResponseDTO getPreparePayment(@PathVariable("merchant_uid") String merchantUid) {
        return paymentService.convertToResponseDTO(iamportService.getPrepare(merchantUid));
    }


    // 결제 취소
    @Operation(
            summary = "결제 취소",
            description = "결제를 취소합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "결제 취소 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "결제 취소 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDTO.class)))
            }
    )
    @PostMapping("/cancel")
    public void CancelPayment(@RequestBody CancelDataRequestDTO request) {
        paymentService.cancelPayment(request);
    }

}