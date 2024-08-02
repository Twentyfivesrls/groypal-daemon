package com.example.groypaldaemon.service;

import com.example.groypaldaemon.model.ResponseWrapper;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import com.paypal.payments.*;
import com.paypal.payments.Capture;
import com.paypal.payments.Money;
import com.paypal.payments.Refund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import twentyfive.twentyfiveadapter.dto.groypalDaemon.SimpleOrderRequest;

import java.io.IOException;

@Service
public class PaymentService {
    @Autowired
    private PayPalHttpClient payPalHttpClient;

    public ResponseWrapper createOrder(SimpleOrderRequest simpleOrderRequest) {
        OrderRequest orderRequest = simpleOrderRequest.toOrderRequest();
        OrdersCreateRequest request = new OrdersCreateRequest();
        request = request.requestBody(orderRequest);
        ResponseWrapper responseWrapper = new ResponseWrapper();
        try{
            HttpResponse<Order> response = this.payPalHttpClient.execute(request);
            responseWrapper.setStatusCode(response.statusCode());
            if(response.statusCode() < 400){
                // here we are in OK status
                responseWrapper.setMessage("OK");
                responseWrapper.setContent(response.result());
            } else if(response.statusCode() >= 400 && response.statusCode() < 500){
                // I don't kwnow how
                responseWrapper.setMessage("KO");
                responseWrapper.setContent(null);
            } else {
                // here we are in an error state
                responseWrapper.setMessage("SERVER ERROR");
                responseWrapper.setContent(null);
            }

        } catch(Exception e){
            e.printStackTrace();
            // here we are in an error state
            responseWrapper.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseWrapper.setMessage(e.getMessage());
            responseWrapper.setContent(null);
        }
            return responseWrapper;

    }

    public ResponseWrapper createOrderOutside(SimpleOrderRequest simpleOrderRequest,PayPalHttpClient payPalHttpClient) {
        OrderRequest orderRequest = simpleOrderRequest.toOrderRequest();
        OrdersCreateRequest request = new OrdersCreateRequest();
        request = request.requestBody(orderRequest);
        ResponseWrapper responseWrapper = new ResponseWrapper();
        try{
            HttpResponse<Order> response = payPalHttpClient.execute(request);
            responseWrapper.setStatusCode(response.statusCode());
            if(response.statusCode() < 400){
                // here we are in OK status
                responseWrapper.setMessage("OK");
                responseWrapper.setContent(response.result());
            } else if(response.statusCode() >= 400 && response.statusCode() < 500){
                // I don't kwnow how
                responseWrapper.setMessage("KO");
                responseWrapper.setContent(null);
            } else {
                // here we are in an error state
                responseWrapper.setMessage("SERVER ERROR");
                responseWrapper.setContent(null);
            }

        } catch(Exception e){
            e.printStackTrace();
            // here we are in an error state
            responseWrapper.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseWrapper.setMessage(e.getMessage());
            responseWrapper.setContent(null);
        }
        return responseWrapper;

    }

    public ResponseWrapper capturePaymentOutside(String orderId, PayPalHttpClient payPalHttpClient) throws IOException {
        OrdersCaptureRequest requestCaptureOrder = new OrdersCaptureRequest(orderId);
        HttpResponse<Order> responseCaptureOrder =  payPalHttpClient.execute(requestCaptureOrder);
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setStatusCode(responseCaptureOrder.statusCode());
        responseWrapper.setMessage("OK");
        responseWrapper.setContent(responseCaptureOrder.result());
        return responseWrapper;
    }

    public ResponseWrapper authorizeRequest(String orderId) throws IOException{
        OrdersAuthorizeRequest ordersAuthorizeRequest = new OrdersAuthorizeRequest(orderId);
        HttpResponse<Order> responseAuthorizeOrder =  payPalHttpClient.execute(ordersAuthorizeRequest);
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setStatusCode(responseAuthorizeOrder.statusCode());
        responseWrapper.setMessage("OK");
        responseWrapper.setContent(responseAuthorizeOrder.result());
        return responseWrapper;
    }

    public ResponseWrapper capturePayment(String orderId) throws IOException {
        OrdersCaptureRequest requestCaptureOrder = new OrdersCaptureRequest(orderId);
        HttpResponse<Order> responseCaptureOrder =  this.payPalHttpClient.execute(requestCaptureOrder);
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setStatusCode(responseCaptureOrder.statusCode());
        responseWrapper.setMessage("OK");
        responseWrapper.setContent(responseCaptureOrder.result());
        return responseWrapper;
    }


    public Order getOrderDetails(String orderId) throws IOException {
        OrdersGetRequest getRequest = new OrdersGetRequest(orderId);
        HttpResponse<Order> response = this.payPalHttpClient.execute(getRequest);
        return response.result();
    }

    public Object createSubscription() throws IOException {
        return null;
    }

    public ResponseWrapper refundPayment(String captureId) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        try {
            Capture capture = getCaptureDetails(captureId);
            Money amount = capture.amount();

            CapturesRefundRequest request = new CapturesRefundRequest(captureId);
            RefundRequest refundRequest = new RefundRequest();
            refundRequest.amount(amount);
            request.requestBody(refundRequest);
            HttpResponse<Refund> response = payPalHttpClient.execute(request);
            responseWrapper.setStatusCode(response.statusCode());
            if (response.statusCode() < 400) {
                responseWrapper.setMessage("OK");
                responseWrapper.setContentRefund(response.result());
            } else if (response.statusCode() >= 400 && response.statusCode() < 500) {
                responseWrapper.setMessage("KO");
                responseWrapper.setContentRefund(null);
            } else {
                responseWrapper.setMessage("SERVER ERROR");
                responseWrapper.setContentRefund(null);
            }
        } catch(Exception e){
            e.printStackTrace();
            responseWrapper.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseWrapper.setMessage(e.getMessage());
            responseWrapper.setContent(null);
        }
        return responseWrapper;
    }

    public Capture getCaptureDetails(String captureId) throws IOException {
        CapturesGetRequest request = new CapturesGetRequest(captureId);
        HttpResponse<Capture> response = payPalHttpClient.execute(request);
        return response.result();
    }
}
