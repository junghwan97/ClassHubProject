package com.example.classhubproject.mapper.payment;

import com.example.classhubproject.data.payment.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {

    void insertPayment(PaymentRequestDTO paymentRequestDTO);

    int getOrdersIdByImpUid(int impUid);

    void cancelPayment(PaymentRequestDTO paymentInfo);

}
