package com.example.classhubproject.service.payment;

import com.example.classhubproject.data.payment.*;
import com.example.classhubproject.mapper.cart.CartMapper;
import com.example.classhubproject.mapper.enrollmentinfo.EnrollmentInfoMapper;
import com.example.classhubproject.mapper.lecture.LectureMapper;
import com.example.classhubproject.mapper.order.OrderMapper;
import com.example.classhubproject.mapper.payment.PaymentMapper;
import lombok.RequiredArgsConstructor;
import com.siot.IamportRestClient.request.*;
import com.siot.IamportRestClient.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final IamportService iamportService;
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;
    private final EnrollmentInfoMapper enrollmentInfoMapper;
    private final LectureMapper lectureMapper;

    // PaymentPrepareResponseDTO 객체로 변환
    public PaymentPrepareResponseDTO convertToResponseDTO(IamportResponse<Prepare> paymentInfo) {
        PaymentPrepareResponseDTO responseData = new PaymentPrepareResponseDTO();
        if (paymentInfo.getResponse() != null) {
            Prepare prepare = paymentInfo.getResponse();
            try {
                Field merchantUidField = Prepare.class.getDeclaredField("merchant_uid");
                merchantUidField.setAccessible(true);
                String merchantUidValue = (String) merchantUidField.get(prepare);

                Field amountField = Prepare.class.getDeclaredField("amount");
                amountField.setAccessible(true);
                BigDecimal amountValue = (BigDecimal) amountField.get(prepare);

                responseData = PaymentPrepareResponseDTO.builder()
                        .code(paymentInfo.getCode())
                        .message(paymentInfo.getMessage())
                        .merchantUid(merchantUidValue)
                        .amount(amountValue)
                        .build();
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("필드 접근 중 오류 발생: {}", e.getMessage());
            }
        }
        return responseData;
    }

    // 결제 정보 저장
    public void addPayment(String impUid) {

        // 포트원 결제 정보 가져오기
        IamportResponse<Payment> paymentResponse = iamportService.paymentByImpUid(impUid);

        // 결제된 금액
        BigDecimal paymentAmount = paymentResponse.getResponse().getAmount();

        int userId = getUserId();

        // 최근 주문 ID 가져오기
        int ordersId = orderMapper.getOrdersIdByUserId(userId);

        // 주문 총 금액 가져오기 (BigDecimal로 형변환)
        BigDecimal totalOrderAmount = new BigDecimal(orderMapper.getTotalPriceByOrdersId(ordersId));

        // 결제 금액이 맞지 않으면 실패 !
        if (paymentAmount.compareTo(totalOrderAmount) != 0) {
            throw new RuntimeException("결제 금액 비일치");
        }

        // 결제된 금액과 주문 총 금액 비교
        Payment payment = paymentResponse.getResponse();
        PaymentRequestDTO paymentInfo = PaymentRequestDTO.builder()
                .ordersId(ordersId)
                .impUid(payment.getImpUid())
                .merchantUid(payment.getMerchantUid())
                .pgProvider(payment.getPgProvider())
                .payMethod(payment.getPayMethod())
                .paymentAmount(payment.getAmount())
                .paymentStatus(payment.getStatus())
                .paidAt(payment.getPaidAt())
                .build();

        log.info("확인용: paymentInfo: {}", paymentInfo);
        paymentMapper.insertPayment(paymentInfo);

        // 장바구니 주문 상태 업데이트
        updateCartStatus(ordersId);

        // 최종 주문 상태 업데이트
        orderMapper.completedOrder(ordersId);

        // 수강 신청 정보 생성
        insertEnrollmentInfo(ordersId);

    }


    // 결제 취소 시 payment_status & cancelled_at 업데이트
    public void cancelPayment(String impUid) {
        CancelData cancelData = new CancelData(impUid, true);
        IamportResponse<Payment> cancelResponse = iamportService.cancelPaymentByImpUid(cancelData);

        if (cancelResponse.getCode() == 0) {
            Payment payment = cancelResponse.getResponse();
            PaymentRequestDTO paymentInfo = PaymentRequestDTO.builder()
                    .impUid(payment.getImpUid())
                    .paymentStatus(payment.getStatus())
                    .cancelledAt(payment.getCancelledAt())
                    .build();

            // 결제 취소 정보 업데이트
            paymentMapper.cancelPayment(paymentInfo);

            // 최종 주문 상태 업데이트
            int ordersId = paymentMapper.getOrdersIdByImpUid(paymentInfo.getOrdersId());
            log.info("주문번호 확인 : ordersId: {}", ordersId);
            orderMapper.cancelOrder(ordersId);
        }
    }


    /*
        // 세션에서 user_id 가져오기
        private int getUserId() {
            return (int) request.getSession().getAttribute("userId");
        }
     */

    //임시 하드코딩
    private int getUserId() {
        return 3;
    }


    // 결제 완료 시 장바구니 주문상태 변경
    private void updateCartStatus(int ordersId) {
        int userId = getUserId();

        // 주문 상세의 class_id 조회
        List<Integer> classIds = orderMapper.getClassIdByOrdersId(ordersId);

        for (int classId : classIds) {
            boolean cartExists = cartMapper.checkCartByClassId(classId, userId);

            if (cartExists) { // 장바구니에 담겨있을 때만 작동
                int cartId = cartMapper.getCartIdByClassId(classId, userId);
                cartMapper.updateOrderStatus(cartId);
            }
        }
    }

    // 수강 신청 정보 생성
    private void insertEnrollmentInfo(int ordersId) {
        int userId = getUserId();

        // 주문 상세의 class_id 조회
        List<Integer> classIds = orderMapper.getClassIdByOrdersId(ordersId);

        for (int classId : classIds) {
            // 강의 별 수강료 조회
            int enrollmentFee = lectureMapper.getClassPrice(classId);

            enrollmentInfoMapper.insertEnrollmentInfo(userId, classId, enrollmentFee);
        }
    }


}
