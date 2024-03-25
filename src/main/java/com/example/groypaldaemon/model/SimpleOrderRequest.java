package com.example.groypaldaemon.model;

import com.paypal.orders.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleOrderRequest {
    private String currency = "EUR";
    private String value;
    private String brandName;
    private String landingPageName = LandingPageType.BILLING.name();
    private String cancelUrl;
    private String returnUrl;
    private String intent = Intent.CAPTURE.name();
    private List<SimpleItem> items;




    public OrderRequest toOrderRequest(){
        // Set up the amount details
        AmountWithBreakdown amount = new AmountWithBreakdown()
                .currencyCode(this.currency)
                .value(this.value) // Set the total amount according to your requirements
                .amountBreakdown(new AmountBreakdown()
                                .itemTotal(new Money()
                                        .currencyCode(this.currency)
                                        .value(this.value)) // Set the item total amount
                        // Add more breakdown details as needed
                );

        // Set up the order request
        OrderRequest orderRequest = new OrderRequest()
                .checkoutPaymentIntent(this.intent)
                .applicationContext(new ApplicationContext()
                        .brandName(this.brandName)
                        .landingPage(this.landingPageName)
                        .cancelUrl(this.cancelUrl)
                        .returnUrl(this.returnUrl))
                .purchaseUnits(List.of(
                        new PurchaseUnitRequest().amountWithBreakdown(amount)
                                .items(this.items.stream().map(SimpleItem::toItem).collect(Collectors.toList()))));

        return orderRequest;
    }

}
