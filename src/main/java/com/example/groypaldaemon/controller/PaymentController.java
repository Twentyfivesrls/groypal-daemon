package com.example.groypaldaemon.controller;

import com.example.groypaldaemon.clients.PaypalClient;
import com.example.groypaldaemon.clients.clientModels.PaypalTokenRequest;
import com.example.groypaldaemon.clients.clientModels.PaypalTokenResponse;
import com.example.groypaldaemon.configuration.ProducerPool;
import com.example.groypaldaemon.model.*;
import com.example.groypaldaemon.service.PaymentService;
import com.google.gson.Gson;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.Order;
import com.twentyfive.authorizationflow.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import twentyfive.twentyfiveadapter.dto.groypalDaemon.PaypalCredentials;
import twentyfive.twentyfiveadapter.dto.groypalDaemon.SimpleItem;
import twentyfive.twentyfiveadapter.dto.groypalDaemon.SimpleOrderRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@CrossOrigin()
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ProducerPool sender;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PaypalClient paypalClient;

    private PayPalHttpClient payPalHttpClient;

    @PostMapping()
    public ResponseEntity<Map<String,Object>> pay(@RequestBody SimpleOrderRequest simpleOrderRequest, @RequestHeader("Payment-App-Id") String paymentAppId){
        ResponseWrapper result = paymentService.createOrder(simpleOrderRequest);
        sendPreorder(result.getContent().id(),simpleOrderRequest.getItems(), paymentAppId);
        Gson gson = new Gson();
        String res = gson.toJson(result);
        Map<String,Object> jsonRes = gson.fromJson(res, Map.class);
        return ResponseEntity.status(result.getStatusCode()).body(jsonRes);
    }
    @PostMapping("/outside")
    public ResponseEntity<Map<String,Object>> payOutside(@RequestBody SimpleOrderRequest simpleOrderRequest,@RequestHeader("Payment-App-Id") String paymentAppId){
        if(simpleOrderRequest.getPaypalCredentials().isDev()){
            payPalHttpClient = new PayPalHttpClient(new PayPalEnvironment.Sandbox(simpleOrderRequest.getPaypalCredentials().getClientId(), simpleOrderRequest.getPaypalCredentials().getClientSecret()));
        } else {
            payPalHttpClient = new PayPalHttpClient(new PayPalEnvironment.Live(simpleOrderRequest.getPaypalCredentials().getClientId(), simpleOrderRequest.getPaypalCredentials().getClientSecret()));
        }
        ResponseWrapper result = paymentService.createOrderOutside(simpleOrderRequest,payPalHttpClient);
        sendPreorder(result.getContent().id(),simpleOrderRequest.getItems(), paymentAppId);
        Gson gson = new Gson();
        String res = gson.toJson(result);
        Map<String,Object> jsonRes = gson.fromJson(res, Map.class);
        return ResponseEntity.status(result.getStatusCode()).body(jsonRes);
    }
    @PostMapping("capture-outside/{orderId}")
    public ResponseEntity<Map<String,Object>> captureRequestOutside(@PathVariable String orderId, @RequestBody PaypalCredentials paypalCredentials) throws IOException {
        if(paypalCredentials.isDev()){
            payPalHttpClient = new PayPalHttpClient(new PayPalEnvironment.Sandbox(paypalCredentials.getClientId(), paypalCredentials.getClientSecret()));
        } else {
            payPalHttpClient = new PayPalHttpClient(new PayPalEnvironment.Live(paypalCredentials.getClientId(), paypalCredentials.getClientSecret()));
        }
        ResponseWrapper result = paymentService.capturePaymentOutside(orderId, payPalHttpClient);
        Gson gson = new Gson();
        String res = gson.toJson(result);
        Map<String,Object> jsonRes = gson.fromJson(res, Map.class);
        // Mandare sulla coda i dati di pagamento
        sendSubscriptionOnKafka(result);
        return ResponseEntity.ok(jsonRes);
    }
    @PostMapping("/refund-outside/{captureId}")
    public ResponseEntity<Map<String,Object>> refundPaymentOutside(@PathVariable String captureId,@RequestBody PaypalCredentials paypalCredentials) throws IOException{
        if(paypalCredentials.isDev()){
            payPalHttpClient = new PayPalHttpClient(new PayPalEnvironment.Sandbox(paypalCredentials.getClientId(), paypalCredentials.getClientSecret()));
        } else {
            payPalHttpClient = new PayPalHttpClient(new PayPalEnvironment.Live(paypalCredentials.getClientId(), paypalCredentials.getClientSecret()));
        }
        ResponseWrapper result = paymentService.refundPaymentOutside(captureId,payPalHttpClient);
        Gson gson = new Gson();
        String res = gson.toJson(result);
        Map<String,Object> jsonRes = gson.fromJson(res, Map.class);
        return ResponseEntity.ok(jsonRes);
    }

    @GetMapping("authorize/{orderId}")
    public ResponseEntity<Map<String,Object>> authorizeRequest(@PathVariable String orderId) throws IOException {
        ResponseWrapper result = paymentService.authorizeRequest(orderId);
        Gson gson = new Gson();
        String res = gson.toJson(result);
        Map<String,Object> jsonRes = gson.fromJson(res, Map.class);
        return ResponseEntity.ok(jsonRes);
    }

    @GetMapping("capture/{orderId}")
    public ResponseEntity<Map<String,Object>> captureRequest(@PathVariable String orderId) throws IOException {
        ResponseWrapper result = paymentService.capturePayment(orderId);
        Gson gson = new Gson();
        String res = gson.toJson(result);
        Map<String,Object> jsonRes = gson.fromJson(res, Map.class);
        // Mandare sulla coda i dati di pagamento
        sendSubscriptionOnKafka(result);
        return ResponseEntity.ok(jsonRes);
    }
    @PostMapping("/refund/{captureId}")
    public ResponseEntity<Map<String,Object>> refundPayment(@PathVariable String captureId) throws IOException{
        ResponseWrapper result = paymentService.refundPayment(captureId);
        Gson gson = new Gson();
        String res = gson.toJson(result);
        Map<String,Object> jsonRes = gson.fromJson(res, Map.class);
        return ResponseEntity.ok(jsonRes);
    }
    private void sendSubscriptionOnKafka(ResponseWrapper response) throws IOException {
        String userId = authenticationService.getId();
        String paymentId = response.getContent().id();
        Order orderData = paymentService.getOrderDetails(paymentId);
        CompletePurchaseMessage message = new CompletePurchaseMessage();
        message.setUserId(userId);
        message.setOrderId(orderData.id());
        message.setPaymentId(paymentId);
        Gson gson = new Gson();
        String result = gson.toJson(message);
        this.sender.send(result, 1, "twentyfive_internal_user_subscriptions");
    }

    private void sendPreorder(String paypalId, List<SimpleItem> items, String paymentAppId){
        PreOrder preOrder = new PreOrder(paypalId, items);
        Gson gson = new Gson();
        String result = gson.toJson(preOrder);
        // "twentyfive_internal_preorders"
        this.sender.send(result, 1, paymentAppId);
    }

    @GetMapping("/testauth")
    public ResponseEntity<PaypalTokenResponse> authenticate(){
        return paypalClient.getToken(new PaypalTokenRequest("client_credentials"));
    }

    @GetMapping("/testarray")
    public ResponseEntity arrayTest(@RequestParam("arrayVal") String arrayVal, @RequestParam("section") String section){
        System.out.println(arrayVal);
        return ResponseEntity.ok(arrayVal);
    }

}
