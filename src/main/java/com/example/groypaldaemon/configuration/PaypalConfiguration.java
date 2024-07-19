package com.example.groypaldaemon.configuration;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class PaypalConfiguration {

    @Value("${paypal.sandbox.clientId}")
    private String clientIdSandbox;

    @Value("${paypal.sandbox.clientSecret}")
    private String clientSecretSandbox;

    @Value("${paypal.live.clientId}")
    private String clientId;

    @Value("${paypal.live.clientSecret}")
    private String clientSecret;

    @Value("${paypal.environment.live}")
    private boolean paypalEnvironmentLive;

    private PayPalHttpClient payPalHttpClient;

    @Bean
    @Scope(WebApplicationContext.SCOPE_APPLICATION)
    @Primary
    public PayPalHttpClient payPalHttpClient() {
        if (payPalHttpClient == null) {
            if (paypalEnvironmentLive) {
                payPalHttpClient = new PayPalHttpClient(new PayPalEnvironment.Live(clientId, clientSecret));
            } else {
                payPalHttpClient = new PayPalHttpClient(new PayPalEnvironment.Sandbox(clientIdSandbox, clientSecretSandbox));
            }
        }
        return payPalHttpClient;
    }

    @Bean
    public PaypalData paypalData(){
        if (paypalEnvironmentLive) {
            return new PaypalData(clientId, clientSecret);
        } else {
            return new PaypalData(clientIdSandbox, clientSecretSandbox);
        }
    }
}
