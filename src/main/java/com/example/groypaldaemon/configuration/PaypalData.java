package com.example.groypaldaemon.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaypalData {
    private String clientId;
    private String clientSecret;
}
