package com.example.classhubproject.mapper.cart;

import com.example.classhubproject.data.cart.CartRequestDTO;
import com.example.classhubproject.data.cart.CartResponseDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface CartMapper {

    void addCart(CartRequestDTO cartRequestDTO);

    boolean checkDuplicate(int userId, int classId);

    List<CartResponseDTO> getCartList(int userId);

    void deleteCart(int cartId);

    void clearCart(int userId);
}
