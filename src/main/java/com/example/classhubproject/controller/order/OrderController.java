package com.example.classhubproject.controller.order;

import com.example.classhubproject.data.common.*;
import com.example.classhubproject.data.order.*;
import com.example.classhubproject.service.lecture.LectureService;
import com.example.classhubproject.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "주문 기능 모음", description = "주문 관련 기능을 처리")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    LectureService lectureService;

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
    public ResponseEntity<ResponseData<Void>> addOrder(@RequestBody List<Integer> classIds) {
        int orderResult = orderService.addOrder(classIds);

        if (orderResult == 1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseData.res(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseMessage.ALREADY_OWNED));
        } else if (orderResult == 2) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.ORDER_SUCCESS));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseData.res(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseMessage.ORDER_FAILED));
        }
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
    public ResponseEntity<ResponseData<List<OrderDetailResponseDTO>>> InProgressOrders(@PathVariable("userId") int userId) {
        List<OrderDetailResponseDTO> onGoingOrderList = orderService.getInProgressOrdersList(userId);
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.ONGOING_ORDER_LIST_SUCCESS, onGoingOrderList));
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
    public ResponseEntity<ResponseData<Void>> deleteOrder(@RequestBody @Schema(description = "강의 ID", example = "{\"classId\": 1}") OrderRequestDTO orderRequestDTO) {
        boolean isDeleted = orderService.deleteOrder(orderRequestDTO.getClassId());
        if(isDeleted) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.DELETE_ORDER_SUCCESS));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseData.res(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseMessage.DELETE_ORDER_ERROR));
        }
    }

    // 주문 내역
    @Operation(
            summary = "특정 사용자의 전체 주문 목록 조회",
            description = "특정 사용자의 전체 주문 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDTO.class)))
            }
    )
    @GetMapping("/list/{userId}")
    public ResponseEntity<ResponseData<List<OrderResponseDTO>>> orderList(@PathVariable("userId") int userId) {
        List<OrderResponseDTO> orderList = orderService.getOrderList(userId);
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.ORDER_LIST_SUCCESS, orderList));
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
    public ResponseEntity<ResponseData<List<OrderDetailResponseDTO>>> orderDetailList(@PathVariable("ordersId") int ordersId) {
        List<OrderDetailResponseDTO> orderDetailList = orderService.getOrderDetailList(ordersId);
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.ORDER_DETAIL_LIST_SUCCESS, orderDetailList));
    }

}