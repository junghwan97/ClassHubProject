package com.example.classhubproject.controller.order;

import com.example.classhubproject.data.order.*;
import com.example.classhubproject.service.lecture.LectureService;
import com.example.classhubproject.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
@Tag(name = "주문 기능 모음", description = "주문 관련 기능을 처리")
public class OrderController {

    private final OrderService orderService;
    private final LectureService lectureService;

    // 주문
    @Operation(
            summary = "주문하기",
            description = "강의 ID를 배열로 받아 상품을 주문합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "주문 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderRequestDTO.class))),
                    @ApiResponse(responseCode = "500", description = "주문 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderRequestDTO.class)))
            }
    )
    @PostMapping("/add")
    public void addOrder(@RequestBody List<Integer> classIds) {
        orderService.addOrder(classIds);
    }

    // 주문 진행 목록 조회
    @Operation(
            summary = "진행중인 주문 목록 조회",
            description = "진행중인 주문 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "진행중인 주문 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDetailResponseDTO.class)))
            }
    )
    @GetMapping("/in-progress/{userId}")
    public List<OrderDetailResponseDTO> InProgressOrders(@PathVariable("userId") Integer userId) {
        return orderService.getInProgressOrdersList(userId);
    }

    // 주문 중인 상품 개별 삭제
    @Operation(
            summary = "진행중인 주문 목록에서 강의 개별 삭제",
            description = "진행중인 주문 목록에서 강의를 개별 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "진행중인 주문 목록에서 강의 개별 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDetailResponseDTO.class)))
            }
    )
    @PostMapping("/delete")
    public void deleteOrder(@RequestBody @Schema(description = "강의 ID", example = "{\"classId\": 1}") OrderRequestDTO orderRequestDTO) {
        orderService.deleteOrder(orderRequestDTO.getClassId());
    }

    // 주문 내역
    @Operation(
            summary = "특정 사용자의 전체 주문 목록 조회",
            description = "특정 사용자의 전체 주문 목록(완료/취소)을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDTO.class)))
            }
    )
    @GetMapping("/list/{userId}")
    public List<OrderResponseDTO> orderList(@PathVariable("userId") Integer userId) {
        return orderService.getOrderList(userId);
    }

    // 주문 상세 내역 조회
    @Operation(
            summary = "특정 주문의 상세 목록 조회",
            description = "특정 주문의 주문 상세 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "주문 상세 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDetailResponseDTO.class)))
            }
    )
    @GetMapping("/detail/{ordersId}")
    public List<OrderDetailResponseDTO> orderDetailList(@PathVariable("ordersId") Integer ordersId) {
        return orderService.getOrderDetailList(ordersId);
    }

}