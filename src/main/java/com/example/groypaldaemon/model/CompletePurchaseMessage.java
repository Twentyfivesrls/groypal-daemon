package com.example.groypaldaemon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletePurchaseMessage {
    private String userId;
    private String orderId;
    private String paymentId;
}
