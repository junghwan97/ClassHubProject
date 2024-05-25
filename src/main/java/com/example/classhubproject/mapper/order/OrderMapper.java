package com.example.classhubproject.mapper.order;

import com.example.classhubproject.data.order.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<OrderResponseDTO> getOrderList(int userId);

    int getOrderDetailCountByOrdersId(int ordersId);

    String getClassNameByOrdersId(int ordersId);

    List<OrderDetailResponseDTO> getOrderDetailList(int ordersId);

    boolean checkHoldClass(int classId, int userId);

    void insertOrder(OrderRequestDTO orderRequestDTO);

    void insertOrderDetail(OrderRequestDTO orderRequestDTO);

    int getOrdersIdByUserId(int userId);

    void completedOrder(int ordersId);

    void cancelOrder(int ordersId);

    int getTotalPriceByOrdersId(int ordersId);

    void deleteInProgressOrderByClassId(int classId);

    List<OrderDetailResponseDTO> getInProgressOrderList(int userId, int ordersId);

    void updateTotalPrice(int ordersId, int totalPrice);

    void deleteOrder(int ordersId);
}