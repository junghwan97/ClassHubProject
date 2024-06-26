package com.example.classhubproject.mapper.cart;

import com.example.classhubproject.data.cart.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface CartMapper {

    void addCart(CartRequestDTO cartRequestDTO);

    boolean isAlreadyCart(int userId, int classId);

    List<CartResponseDTO> getCartList(int userId);

    void deleteCart(int cartId);

    void clearCart(int userId);

    void updateOrderStatus(int cartId);

    boolean hasClassInCart(int classId, int userId);

    int getCartIdByClassId(int classId, int userId);

}
