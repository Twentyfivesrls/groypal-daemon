package com.example.groypaldaemon.model;

import com.paypal.orders.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWrapper {
    private int statusCode;
    private Order content;
    private String message;
}
