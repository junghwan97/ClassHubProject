package com.example.classhubproject.service.cart;

import com.example.classhubproject.data.cart.*;
import com.example.classhubproject.mapper.cart.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;

    // 장바구니 담기
    public void addCart(CartRequestDTO cartRequestDTO) {
        // 장바구니 동일 상품 체크
        if (checkDuplicate(cartRequestDTO)) {
            throw new RuntimeException("이미 장바구니에 동일한 상품이 있습니다.");
        } else {
            cartMapper.addCart(cartRequestDTO);
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
    public void deleteCart(int cartId) {
        cartMapper.deleteCart(cartId);
    }

    // 장바구니 비우기
    public void clearCart(int userId) {
        cartMapper.clearCart(userId);
    }

}
