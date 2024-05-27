package com.example.classhubproject.service.order;

import com.example.classhubproject.data.order.*;
import com.example.classhubproject.mapper.lecture.LectureMapper;
import com.example.classhubproject.mapper.order.OrderMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final LectureMapper lectureMapper;
    private final HttpServletRequest request;


    // 진행중인 주문 목록 조회
    public List<OrderDetailResponseDTO> getInProgressOrdersList(int userId) {
        // 특정 회원의 가장 최근에 생성된 orders_id
        int ordersId = orderMapper.getOrdersIdByUserId(userId);

        // 진행중인 주문 목록 조회
        return orderMapper.getInProgressOrderList(userId, ordersId);
    }

    // 진행중인 주문의 강의 개별 삭제
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteOrder(int classId) {
        int userId = getUserId();

        List<OrderDetailResponseDTO> inProgressOrders = getInProgressOrdersList(userId);
        List<Integer> classIds = new ArrayList<>();
        for (OrderDetailResponseDTO order : inProgressOrders) {
            if (order.getClassId() != classId) {
                classIds.add(order.getClassId());
            }
        }

        // 주문에서 해당 강의 삭제
        orderMapper.deleteInProgressOrderByClassId(classId);

        // 총 주문 금액 계산
        int totalPrice = calculateTotalPrice(classIds);

        // 주문 아이디 확인
        int ordersId = orderMapper.getOrdersIdByUserId(userId);

        // 총 주문 금액 0일 때 해당 주문 정보 삭제
        if (totalPrice == 0) {
            orderMapper.deleteOrder(ordersId);
        } else {
            // 총 주문 금액 업데이트
            orderMapper.updateTotalPrice(ordersId, totalPrice);
        }
    }

    // 특정 사용자의 전체 주문 목록
    public List<OrderResponseDTO> getOrderList(int userId) {
        List<OrderResponseDTO> orderList = orderMapper.getOrderList(userId);

        List<OrderResponseDTO> updatedOrderList = new ArrayList<>();

        for (OrderResponseDTO order : orderList) {
            int ordersId = order.getOrdersId();
            String orderName = createOrderName(ordersId);

            OrderResponseDTO updatedOrder = OrderResponseDTO.builder()
                    .ordersId(order.getOrdersId())
                    .userId(order.getUserId())
                    .totalPrice(order.getTotalPrice())
                    .finalOrderStatus(order.getFinalOrderStatus())
                    .regdate(order.getRegdate())
                    .orderName(orderName)
                    .build();

            // 목록에 추가
            updatedOrderList.add(updatedOrder);

        }
        return updatedOrderList;
    }

    // 주문 상세 목록
    public List<OrderDetailResponseDTO> getOrderDetailList(int ordersId) {
        return orderMapper.getOrderDetailList(ordersId);
    }

    // 주문별 주문명 생성
    private String createOrderName(int ordersId) {
        int orderDetailCount = orderMapper.getOrderDetailCountByOrdersId(ordersId);
        String className = orderMapper.getClassNameByOrdersId(ordersId);

        String orderName;

        if (orderDetailCount == 1) {
            orderName = className;
        } else {
            orderName = className + " 외 " + (orderDetailCount - 1) + "건";
        }

        return orderName;
    }

    // 주문하기
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public boolean addOrder(List<Integer> classIds) {
        // 세션에서 userId 조회
        int userId = getUserId();

        // 이미 보유한 강의인지 확인
        boolean alreadyHold = checkHoldClass(classIds);
        if (alreadyHold) {
            throw new RuntimeException("이미 보유한 강의가 포함되어 있습니다.");
        } else {
            // 총 주문 금액 계산
            int totalPrice = calculateTotalPrice(classIds);

            // 주문 정보 생성
            OrderRequestDTO orders = createOrder(userId, totalPrice);

            // 주문 추가
            int ordersId = insertOrder(orders);

            // 주문 상세 추가
            insertOrderDetails(classIds);

            return true;
        }
    }

    // 세션에서 userId 조회
    /*
    private int getUserId() {
        return (int) request.getSession().getAttribute("userId");
    }
     */

    // 테스트용 하드코딩 (임시)
    private int getUserId() {
        return 3;
    }

    // 이미 보유한 강의인지 확인
    private boolean checkHoldClass(List<Integer> classIds) {
        int userId = getUserId();

        for (int classId : classIds) {
            boolean alreadyOrder = orderMapper.checkHoldClass(classId, userId);
            if (alreadyOrder) {
                return true;
            }
        }
        return false;
    }

    // 총 주문 금액 계산
    private int calculateTotalPrice(List<Integer> classIds) {
        int totalPrice = 0;
        for (int classId : classIds) {
            int classPrice = lectureMapper.getClassPrice(classId);
            totalPrice += classPrice;
        }
        return totalPrice;
    }

    // 주문 정보 생성
    private OrderRequestDTO createOrder(int userId, int totalPrice) {
        return OrderRequestDTO.builder()
                .userId(userId)
                .totalPrice(totalPrice)
                .build();
    }

    // 주문 추가
    private int insertOrder(OrderRequestDTO orderRequestDTO) {
        orderMapper.insertOrder(orderRequestDTO);
        return orderRequestDTO.getOrdersId();
    }

    // 주문 상세 추가
    private void insertOrderDetails(List<Integer> classIds) {
        // 세션에서 userId 조회
        int userId = getUserId();

        // 특정 회원의 가장 최근에 생성된 orders_id
        int ordersId = orderMapper.getOrdersIdByUserId(userId);

        for (int classId : classIds) {
            OrderRequestDTO orderDetailDto = OrderRequestDTO.builder()
                    .ordersId(ordersId)
                    .classId(classId).build();
            orderMapper.insertOrderDetail(orderDetailDto);
        }
    }

    // 주문 완료
    public boolean completedOrder(int ordersId) {
        try {
            orderMapper.completedOrder(ordersId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 주문 취소
    public boolean cancelOrder(int ordersId) {
        try {
            orderMapper.cancelOrder(ordersId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}