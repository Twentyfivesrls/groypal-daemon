package com.example.groypaldaemon.clients.clientModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaypalTokenRequest {
    private String grant_type;
}
