package com.example.classhubproject.controller.payment;

import com.example.classhubproject.data.common.*;
import com.example.classhubproject.data.payment.*;
import com.example.classhubproject.service.payment.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.*;
import com.siot.IamportRestClient.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "결제 기능 모음", description = "결제 관련 기능을 처리")
public class PaymentController {

    @Autowired
    private IamportClient iamportClient;
    @Autowired
    private PaymentService paymentService;

    @Value("${iamport.key}")
    private String apiKey;
    @Value("${iamport.secret}")
    private String secretKey;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    // 사후 검증 후 결제 정보 저장
    @Operation(
            summary = "결제 정보 저장하기",
            description = "아임포트 식별자로 사후 검증 후 결제 정보를 저장합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "결제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "결제 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDTO.class)))
            }
    )
    @PostMapping("/add")
    public ResponseEntity<ResponseData<Void>> addPayment(@RequestParam("imp_uid") String impUid) {
        int paymentResult = paymentService.addPayment(impUid);

        if (paymentResult == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseData.res(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseMessage.PAYMENT_FAILED));
        } else if (paymentResult == 1) {
            return ResponseEntity.ok()
                    .body(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.PAYMENT_SUCCESS));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseData.res(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseMessage.PAYMENT_ERROR));
        }
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
    public ResponseEntity<ResponseData<IamportResponse<Payment>>> getPaymentByImpUid(@PathVariable("imp_uid") String imp_uid) {
        try {
            IamportResponse<Payment> response = iamportClient.paymentByImpUid(imp_uid);
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.PAYMENT_RETRIEVE_SUCCESS, response));
        } catch (IamportResponseException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseData.res(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseMessage.PAYMENT_RETRIEVE_ERROR));
        }
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
    public ResponseEntity<ResponseData<Void>> preparePayment(@RequestParam("merchant_uid") String merchantUid, @RequestParam("amount") BigDecimal amount) {
        try {
            PrepareData prepareData = new PrepareData(merchantUid, amount);
            iamportClient.postPrepare(prepareData);
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.PAYMENT_PREPARE_SUCCESS));
        } catch (IamportResponseException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseData.res(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseMessage.PAYMENT_PREPARE_ERROR));
        }
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
    public ResponseEntity<ResponseData<PaymentPrepareResponseDTO>> getPreparePayment(@PathVariable("merchant_uid") String merchantUid) {
        log.info("사전 결제 정보를 조회 중, merchant_uid: {}", merchantUid);
        try {
            IamportResponse<Prepare> paymentInfo = iamportClient.getPrepare(merchantUid);
            log.info("사전 결제 정보를 조회 성공함, merchant_uid: {}", merchantUid);
            PaymentPrepareResponseDTO responseData = paymentService.convertToResponseDTO(paymentInfo);
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.PREPARE_AMOUNT_SUCCESS, responseData));
        } catch (IamportResponseException | IOException e) {
            log.error("사전 결제 정보 조회 중 오류가 발생함, merchant_uid: {}", merchantUid, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseData.res(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseMessage.PREPARE_AMOUNT_ERROR));
        }
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
    public ResponseEntity<ResponseData<Void>> CancelPayment(@RequestParam("imp_uid") @Schema(description = "아임포트 식별자", required = true, example = "{\"imp_uid\": 1}") String impUid) {
        int cancelResult = paymentService.cancelPayment(impUid);

        if (cancelResult == 1) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.PAYMENT_CANCEL_SUCCESS));
        } else if (cancelResult == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(ResponseData.res(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseMessage.PAYMENT_CANCEL_FAILED));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(ResponseData.res(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseMessage.PAYMENT_CANCEL_ERROR));
        }
    }

}