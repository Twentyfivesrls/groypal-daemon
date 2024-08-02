package com.example.groypaldaemon.model;

import com.paypal.orders.Order;
import com.paypal.payments.Refund;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWrapper<T> {
    private int statusCode;
    private Order content;
    private Refund contentRefund;
    private String message;
}
