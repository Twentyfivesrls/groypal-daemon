package com.example.groypaldaemon.configuration;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean
    public PayPalHttpClient payPalHttpClient() {
        if(paypalEnvironmentLive) {
            return new PayPalHttpClient(
                    new PayPalEnvironment.Live(clientId, clientSecret)
            );
        } else {
            return new PayPalHttpClient(
                    new PayPalEnvironment.Sandbox(clientIdSandbox, clientSecretSandbox)
            );
        }
    }

    @Bean
    public PaypalData paypalData(){
        if(paypalEnvironmentLive) {
            return new PaypalData(clientId, clientSecret);
        } else {
            return new PaypalData(clientIdSandbox, clientSecretSandbox);
        }
    }



}
