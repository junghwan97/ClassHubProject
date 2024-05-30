package com.example.classhubproject.service.payment;

import com.example.classhubproject.data.payment.*;
import com.example.classhubproject.exception.ClassHubErrorCode;
import com.example.classhubproject.exception.ClassHubException;
import com.example.classhubproject.mapper.cart.CartMapper;
import com.example.classhubproject.mapper.enrollmentinfo.EnrollmentInfoMapper;
import com.example.classhubproject.mapper.lecture.LectureMapper;
import com.example.classhubproject.mapper.order.OrderMapper;
import com.example.classhubproject.mapper.payment.PaymentMapper;
import jakarta.servlet.http.HttpServletRequest;
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
    private final HttpServletRequest request;

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
    public void addPayment(int userId, String impUid) {

        // 포트원 결제 정보 가져오기
        IamportResponse<Payment> paymentResponse = iamportService.paymentByImpUid(impUid);

        // 결제된 금액과 주문 총 금액 비교
        validatePayment(userId, paymentResponse);

        // 결제 정보 생성
        insertPaymentInfo(userId, paymentResponse);

        // 장바구니 주문 상태 업데이트
        updateCartStatus(userId);

        // 최종 주문 상태 업데이트
        completedOrder(userId);

        // 수강 신청 정보 생성
        insertEnrollmentInfo(userId);

    }


    // 결제 취소 시 payment_status & cancelled_at 업데이트
    public void cancelPayment(CancelDataRequestDTO request) {
        String impUid = request.getImpUid();
        CancelData cancelData = new CancelData(impUid, true);

        // 포트원 결제 취소 정보 가져오기
        Payment payment = iamportService.cancelPaymentByImpUid(cancelData);

        // 결제 취소 정보 업데이트
        updatePayment(payment);

        // 취소 주문 ID 조회
        int ordersId = getOrdersId(impUid);

        // 최종 주문 상태 업데이트
        updateOrderCanceled(ordersId);
    }

    // 세션에서 user_id 가져오기
//    private int getUserId() {
//        return (int) request.getSession().getAttribute("userId");
//    }

    // 특정 회원의 가장 최근에 생성된 주문ID 조회
    private int getOrdersIdByUserId(int userId) {
        return orderMapper.getOrdersIdByUserId(userId);
    }

    // 결제 정보 확인
    private void validatePayment(int userId, IamportResponse<Payment> paymentResponse) {
        // 포트원 사전 결제 금액과 DB 결제 금액 비교
        BigDecimal receivedPaymentAmount = paymentResponse.getResponse().getAmount();

        // 최근 주문 ID 가져오기
        int ordersId = getOrdersIdByUserId(userId);
        BigDecimal expectedOrderTotal = new BigDecimal(orderMapper.getTotalPriceByOrdersId(ordersId));

        if (receivedPaymentAmount.compareTo(expectedOrderTotal) != 0) {
            throw new ClassHubException(ClassHubErrorCode.HAS_PAYMENT_AMOUNT_MISMATCH);
        }
    }


    // 결제 정보 생성
    private void insertPaymentInfo(int userId, IamportResponse<Payment> paymentResponse) {
        int ordersId = getOrdersIdByUserId(userId);

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

        paymentMapper.insertPayment(paymentInfo);
    }


    // 결제 완료 시 장바구니 주문상태 변경
    private void updateCartStatus(int userId) {
        //int userId = getUserId();
        int ordersId = getOrdersIdByUserId(userId);

        // 주문 상세의 강의 조회
        List<Integer> classIds = getClassIds(ordersId);

        classIds.stream()
                .filter(classId -> cartMapper.hasClassInCart(classId, userId))
                .map(classId -> cartMapper.getCartIdByClassId(classId, userId))
                .forEach(cartMapper::updateOrderStatus);
    }

    // 최종 주문 상태 업데이트
    private void completedOrder(int userId) {
        int ordersId = getOrdersIdByUserId(userId);

        orderMapper.completedOrder(ordersId);
    }

    // 수강 신청 정보 생성
    private void insertEnrollmentInfo(int userId) {
        //int userId = getUserId();
        int ordersId = getOrdersIdByUserId(userId);

        // 주문 상세의 강의 조회
        List<Integer> classIds = getClassIds(ordersId);

        classIds.stream()
                .forEach(classId ->
                        enrollmentInfoMapper.insertEnrollmentInfo(userId, classId, getEnrollmentFee(classId)));
    }

    // 주문 상세의 강의 조회
    private List<Integer> getClassIds(int ordersId) {
        return orderMapper.getClassIdByOrdersId(ordersId);
    }


    // 결제 취소 후 결제 정보 변경
    private void updatePayment(Payment payment) {
        PaymentRequestDTO paymentInfo = PaymentRequestDTO.builder()
                .impUid(payment.getImpUid())
                .paymentStatus(payment.getStatus())
                .cancelledAt(payment.getCancelledAt())
                .build();

        paymentMapper.cancelPayment(paymentInfo);
    }

    // 결제 취소 후 최종 주문 상태 변경
    private void updateOrderCanceled(int ordersId) {
        orderMapper.cancelOrder(ordersId);
    }

    // 결제 정보에서 주문 ID 조회
    private int getOrdersId(String impUid) {
        return paymentMapper.getOrdersIdByImpUid(impUid);
    }

    // 수강료 조회
    private int getEnrollmentFee(int classId) {
        return lectureMapper.getClassPrice(classId);
    }

}