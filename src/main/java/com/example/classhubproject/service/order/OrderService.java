package com.example.classhubproject.service.order;

import com.example.classhubproject.data.order.*;
import com.example.classhubproject.mapper.cart.CartMapper;
import com.example.classhubproject.mapper.lecture.LectureMapper;
import com.example.classhubproject.mapper.order.OrderMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    LectureMapper lectureMapper;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    HttpServletRequest request;


    // 주문 목록
    public List<OrderResponseDTO> getOrderList(int userId) {
        List<OrderResponseDTO> orderList = orderMapper.getOrderList(userId); // orderRequest

        List<OrderResponseDTO> updatedOrderList = new ArrayList<>();

        // 생성된 주문명을 OrderReponseDTO의 orderName에 집어넣어서 목록 보여주기
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
    @Transactional(rollbackFor = Exception.class)
    public int addOrder(List<Integer> classIds) {
        try {
            // 세션에서 userId 조회
            int userId = getUserId();

            // 보유한 강의인지 확인
            boolean alreadyHold = checkHoldClass(classIds);
            if (alreadyHold) {
                return 1;
            } else {

                // 총 주문 금액 계산
                int totalPrice = calculateTotalPrice(classIds);

                // 주문 정보 생성
                OrderRequestDTO orders = createOrder(userId, totalPrice);

                // 주문 추가
                int ordersId = addOrder(orders);

                // 주문 상세 추가
                addOrderDetails(classIds);

                // 주문완료 시 Cart order_status 1로 변경
                updateCartStatus(classIds);

                return 2;
            }
        } catch (Exception e) {
            System.out.println("주문 처리 중 오류 발생: " + e.getMessage());
            return 0;
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
        return 2;
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
    private int addOrder(OrderRequestDTO orderRequestDTO) {
        orderMapper.insertOrder(orderRequestDTO);
        return orderRequestDTO.getOrdersId();
    }

    // 주문 상세 추가
    private void addOrderDetails(List<Integer> classIds) {
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

    // 주문완료 시 장바구니 주문상태 변경
    private void updateCartStatus(List<Integer> classIds) {
        int userId = getUserId();
        for (int classId : classIds) {
            boolean cartExists = cartMapper.checkCartByClassId(classId, userId);

            if (cartExists) { // 장바구니에 담겨있을 때만 작동
                int cartId = cartMapper.getCartIdByClassId(classId, userId);
                cartMapper.updateOrderStatus(cartId);
            }
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