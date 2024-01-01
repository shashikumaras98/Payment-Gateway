package com.payment.paymentStripes.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    // http://localhost:800/api/payment/create-payment
    @PostMapping("/create-payment")
    public String createPaymentIntent(@RequestParam Map<String, Object> payload) { // using JSON

            // Extract the amount from the payload
            Integer amount = (Integer) payload.get("amount");

            //http://localhost:800/api/payment/create-payment?amount=50000
            //public String createPaymentIntent(@RequestParam Integer amount) { //without JSON(send amount using URL)

        Stripe.apiKey = stripeApiKey;

        try {

            // Create a charge
            PaymentIntent intent = PaymentIntent.create(
                    new PaymentIntentCreateParams.Builder()
                            .setCurrency("usd")
                            .setAmount((long) amount * 100 ) // amount in cents
                            .build()
            );

            // Handle success
            return generateResponse("Payment successful. Charge ID: " + intent.getClientSecret());
        } catch (StripeException e) {
            // Handle failure
            return generateResponse("Payment failed. Error: " + e.getMessage());
        }
    }

    private String generateResponse(String clientSecret) {
        return "{\"clientSecret\":\"" + clientSecret + "\"}";
    }
}
