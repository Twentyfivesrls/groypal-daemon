package com.example.groypaldaemon.clients;

import com.example.groypaldaemon.clients.clientModels.PaypalTokenRequest;
import com.example.groypaldaemon.clients.clientModels.PaypalTokenResponse;
import com.example.groypaldaemon.configuration.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PaypalClient", url = "${paypal.baseUrl.sandbox}/v1", configuration = FeignConfiguration.class)
public interface PaypalClient {

    @PostMapping(value = "/oauth2/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<PaypalTokenResponse> getToken(@RequestBody PaypalTokenRequest data);

}
