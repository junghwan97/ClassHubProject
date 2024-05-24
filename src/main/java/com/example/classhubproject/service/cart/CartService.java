package com.example.classhubproject.service.cart;

import com.example.classhubproject.data.cart.*;
import com.example.classhubproject.data.lecture.ClassResponseDTO;
import com.example.classhubproject.mapper.cart.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    @Autowired
    CartMapper cartMapper;

    // 장바구니 담기
    public boolean addCart(CartRequestDTO cartRequestDTO) {
        // 장바구니 동일 상품 체크
        if (checkDuplicate(cartRequestDTO)) {
            return false;
        } else {
            cartMapper.addCart(cartRequestDTO);
            return true;
        }
    }

    // 이미 담긴 상품인지 체크
    public boolean checkDuplicate(CartRequestDTO cartRequestDTO) {
        return cartMapper.checkDuplicate(cartRequestDTO.getUserId(), cartRequestDTO.getClassId());
    }

    // 장바구니 조회
    public List<CartResponseDTO> getCartList(int userId) {
        return cartMapper.getCartList(userId);
    }

    // 장바구니 상품 개별 삭제
    public boolean deleteCart(int cartId) {
        try {
            cartMapper.deleteCart(cartId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 장바구니 비우기
    public boolean clearCart(int userId) {
        try {
            cartMapper.clearCart(userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
