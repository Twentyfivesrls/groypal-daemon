package com.example.groypaldaemon.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPayment {
    private String id;
    private String userId;
    private Map<String,Object> paymentData; // todo currently saved as Map because we don't know all the data about the payments
}
