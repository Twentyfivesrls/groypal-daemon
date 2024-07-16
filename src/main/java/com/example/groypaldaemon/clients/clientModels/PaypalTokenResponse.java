package com.example.groypaldaemon.clients.clientModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaypalTokenResponse {
    private String scope;
    private String access_token;
    private String token_type;
    private String app_id;
    private long expires_in;
    private String nonce;
}
