package com.bang.WebBanHang_Project.client;

import com.bang.WebBanHang_Project.common.PaymentGatewayStatus;
import com.bang.WebBanHang_Project.config.PaymentGatewayConfig;
import com.bang.WebBanHang_Project.controller.request.PaymentGatewayRequest;
import com.bang.WebBanHang_Project.controller.request.RefundGatewayRequest;
import com.bang.WebBanHang_Project.controller.response.PaymentGatewayResponse;
import com.bang.WebBanHang_Project.controller.response.RefundGatewayResponse;
import com.bang.WebBanHang_Project.controller.response.SignedResponse;
import com.bang.WebBanHang_Project.exception.PaymentGatewayException;
import org.springframework.http.*;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Slf4j(topic = "PAYMENT-GATEWAY-CLIENT")
@RequiredArgsConstructor
public class PaymentGatewayClient {

    private final RestTemplate restTemplate;
    private final PaymentGatewayConfig config;
    private final ObjectMapper objectMapper;

    public PaymentGatewayResponse processPayment(PaymentGatewayRequest request){
        log.info("Sending payment request to gateway: {}", request.getOrderId());

        try {
            HttpHeaders headers = createHeaders();
            String requestUrl = config.getBaseUrl() + "/api/v1/payments";

            PaymentGatewayRequest newRequest = PaymentGatewayRequest.builder()
                    .merchantId(config.getMerchantId())
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .orderId(request.getOrderId())
                    .paymentMethod(request.getPaymentMethod())
                    .returnUrl(config.getReturnUrl())
                    .notifyUrl(config.getNotifyUrl())
                    .timestamp(System.currentTimeMillis())
                    .build();

            newRequest.setSignature(generateSignature(request));

            HttpEntity<PaymentGatewayRequest> entity = new HttpEntity<>(newRequest, headers);;

            ResponseEntity<PaymentGatewayResponse> response = restTemplate.exchange(
                    requestUrl,
                    HttpMethod.POST,
                    entity,
                    PaymentGatewayResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null){

                if (verifySignature(response.getBody())){
                    throw new PaymentGatewayException("Invalid response signature");
                }

                return PaymentGatewayResponse.builder()
                        .transactionId(response.getBody().getTransactionId())
                        .status(response.getBody().getStatus())
                        .message(response.getBody().getMessage())
                        .build();

            } else {
                throw new PaymentGatewayException("Payment gateway returned error status: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            log.error("Failed to communicate with payment gateway: {}", e.getMessage());
            throw new PaymentGatewayException("Communication with payment gateway failed", e);
        } catch (Exception e) {
            log.error("Unexpected error during payment processing: {}", e.getMessage());
            throw new PaymentGatewayException("Payment processing failed", e);
        }
    }

    public RefundGatewayResponse processRefund(RefundGatewayRequest request){
        log.info("Sending refund request to gateway for transaction: {}", request);

        try {
            HttpHeaders headers = createHeaders();
            String requestUrl = config.getBaseUrl() + "/api/v1/refunds";

            RefundGatewayRequest newRequest = RefundGatewayRequest.builder()
                    .merchantId(config.getMerchantId())
                    .transactionId(request.getTransactionId())
                    .reason(request.getReason())
                    .amount(request.getAmount())
                    .timestamp(System.currentTimeMillis())
                    .build();

            newRequest.setSignature(generateSignature(newRequest));

            HttpEntity<RefundGatewayRequest> entity = new HttpEntity<>(newRequest,headers);

            ResponseEntity<RefundGatewayResponse> response = restTemplate.exchange(
                    requestUrl,
                    HttpMethod.POST,
                    entity,
                    RefundGatewayResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null){
                if (verifySignature(response.getBody())){
                    throw new PaymentGatewayException("Invalid refund response signature");
                }

                return RefundGatewayResponse.builder()
                        .refundId(response.getBody().getRefundId())
                        .message(response.getBody().getMessage())
                        .status(response.getBody().getStatus())
                        .build();
            } else {
                throw new PaymentGatewayException("Refund request failed with status: {}" + response.getBody());
            }
        } catch (Exception e){
            log.error("Failed to process refund: {}", e.getMessage());
            throw new PaymentGatewayException("Refund processing failed", e);
        }

    }

    public PaymentGatewayStatus checkStatus(String transactionId){
        log.debug("Checking payment status for transaction: {}", transactionId);

        try {
            HttpHeaders headers = createHeaders();
            String requestUrl = config.getBaseUrl() + "/api/v1/payment/" + transactionId;

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<PaymentGatewayResponse> response = restTemplate.exchange(
                    requestUrl,
                    HttpMethod.POST,
                    entity,
                    PaymentGatewayResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null){
                return response.getBody().getStatus();
            } else {
                throw new PaymentGatewayException("Failed to get payment status");
            }
        } catch (Exception e){
            log.error("Error checking payment status: {}", e.getMessage());
            throw new PaymentGatewayException("Failed to check payment status", e);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", config.getApiKey());
        headers.set("X-MERCHANT-ID", config.getMerchantId());
        return headers;
    }


    private String generateSignature(Object request){
        try {
            String dataToSign = objectMapper.writeValueAsString(request);
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(
                    config.getSecretKey().getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );
            hmac.init(secretKey);
            byte[] hash = hmac.doFinal(dataToSign.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch(Exception e){
            throw new PaymentGatewayException("Failed to generate signature", e);
        }
    }

    private boolean verifySignature(SignedResponse response){
        String receivedSignature = response.getSignature();
        response.setSignature(null);
        String calculatedSignature = generateSignature(response);
        return !receivedSignature.equals(calculatedSignature);
    }
}
