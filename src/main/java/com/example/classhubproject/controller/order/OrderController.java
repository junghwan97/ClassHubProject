package com.example.classhubproject.controller.order;

import com.example.classhubproject.data.common.*;
import com.example.classhubproject.data.order.*;
import com.example.classhubproject.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    // 주문
    @Operation(
            summary = "주문하기",
            description = "상품을 주문합니다.",
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

    // 주문 내역
    @Operation(
            summary = "주문 목록",
            description = "특정 사용자의 주문 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDTO.class)))
            }
    )
    @GetMapping("/list/{userId}")
    public ResponseEntity<ResponseData<List<OrderResponseDTO>>> orderList(@PathVariable("userId") int userId) {
        List<OrderResponseDTO> orderList = orderService.getOrderList(userId);
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.ORDER_LIST_SUCCESS, orderList));
    }

    // 주문 상세 내역
    @Operation(
            summary = "주문 상세 목록",
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

    // 주문 취소
    @Operation(
            summary = "주문 취소",
            description = "주문을 취소합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "주문 취소 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderRequestDTO.class))),
                    @ApiResponse(responseCode = "500", description = "주문 취소 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderRequestDTO.class)))
            }
    )
    @PostMapping("/cancel")
    public ResponseEntity<ResponseData<Void>> cancelOrder(@RequestBody @Schema(description = "주문 ID", required = true, example = "{\"ordersId\": 1}") OrderRequestDTO orderRequestDTO) {
        boolean cancelToOrder = orderService.cancelOrder(orderRequestDTO.getOrdersId());

        if (cancelToOrder) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.ORDER_CANCEL_SUCCESS));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body((ResponseData.res(HttpStatus.CONFLICT.value(), ResponseMessage.ORDER_CANCEL_FAILED)));
        }
    }
}