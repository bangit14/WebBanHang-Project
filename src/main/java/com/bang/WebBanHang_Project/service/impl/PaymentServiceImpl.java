package com.bang.WebBanHang_Project.service.impl;

import com.bang.WebBanHang_Project.client.PaymentGatewayClient;
import com.bang.WebBanHang_Project.common.PaymentGatewayStatus;
import com.bang.WebBanHang_Project.common.PaymentProviderType;
import com.bang.WebBanHang_Project.common.PaymentStatus;
import com.bang.WebBanHang_Project.common.RefundStatus;
import com.bang.WebBanHang_Project.controller.request.PaymentGatewayRequest;
import com.bang.WebBanHang_Project.controller.request.PaymentRequest;
import com.bang.WebBanHang_Project.controller.request.PaymentUpdateRequest;
import com.bang.WebBanHang_Project.controller.request.RefundGatewayRequest;
import com.bang.WebBanHang_Project.controller.response.PaymentGatewayResponse;
import com.bang.WebBanHang_Project.controller.response.PaymentResponse;
import com.bang.WebBanHang_Project.controller.response.RefundGatewayResponse;
import com.bang.WebBanHang_Project.exception.*;
import com.bang.WebBanHang_Project.model.Payment;
import com.bang.WebBanHang_Project.model.Refund;
import com.bang.WebBanHang_Project.repository.PaymentRepository;
import com.bang.WebBanHang_Project.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@Slf4j(topic = "PAYMENT-SERVICE")
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGatewayClient paymentGatewayClient;

    @Override
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        log.info("Processing payment for order: {}", paymentRequest);

        try{

            validatePaymentRequest(paymentRequest);

            Payment payment = new Payment();
            payment.setOrderId(paymentRequest.getOrderId());
            payment.setAmount(paymentRequest.getAmount());
            payment.setCurrency(paymentRequest.getCurrency());
            payment.setPaymentMethod(paymentRequest.getPaymentMethod());
            payment.setStatus(PaymentStatus.PENDING);
            payment.setCreatedAt(LocalDateTime.now());

            PaymentGatewayResponse gatewayResponse = paymentGatewayClient.processPayment(
                    PaymentGatewayRequest.builder()
                            .amount(paymentRequest.getAmount())
                            .currency(paymentRequest.getCurrency())
                            .paymentMethod(paymentRequest.getPaymentMethod())
                            .orderId(paymentRequest.getOrderId())
                            .build()
            );

            payment.setGatewayTransactionId(gatewayResponse.getTransactionId());
            payment.setStatus(mapGatewayStatus(gatewayResponse.getStatus()));
            payment.setProcessedAt(LocalDateTime.now());
            paymentRepository.save(payment);

            return PaymentResponse.builder()
                    .id(payment.getId())
                    .status(payment.getStatus())
                    .gatewayTransactionId(payment.getGatewayTransactionId())
                    .build();
        } catch (PaymentValidationException e) {
            log.error("Payment validation failed: {}", e.getMessage());
            throw new PaymentProcessingException("Invalid payment request", e);
        } catch (PaymentGatewayException e) {
            log.error("Payment gateway error: {}", e.getMessage());
            throw new PaymentProcessingException("Payment gateway error", e);
        }
    }

    @Override
    public void refundPayment(String paymentId) {
        log.info("Processing refund for payment: {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId);

        if(!canRefund(payment)){
            throw new RefundNotAllowedException("Payment cannot be refunded in current status: " + payment.getStatus());
        }

        try {

            RefundGatewayResponse gatewayResponse = paymentGatewayClient.processRefund(
                    RefundGatewayRequest.builder()
                            .transactionId(payment.getGatewayTransactionId())
                            .amount(payment.getAmount())
                            .reason("Order Cancelled")
                            .build()
            );

            Refund refund = Refund.builder()
                    .paymentId(paymentId)
                    .amount(payment.getAmount())
                    .status(RefundStatus.COMPLETED)
                    .gatewayRefundId(gatewayResponse.getRefundId())
                    .refundedAt(LocalDateTime.now())
                    .build();

            payment.setStatus(PaymentStatus.REFUNDED);
            payment.setRefund(refund);
            paymentRepository.save(payment);
        } catch (PaymentGatewayException e) {
            log.error("Refund processing failed: {}", e.getMessage());
            throw new RefundProcessingException("Failed to process refund", e);
        }

    }

    @Override
    public PaymentStatus checkPaymentStatus(String paymentId) {
        log.debug("Checking payment status for: {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId);

        PaymentGatewayStatus gatewayStatus = paymentGatewayClient.checkStatus(payment.getGatewayTransactionId());
        PaymentStatus currentStatus = mapGatewayStatus(gatewayStatus);

        if (payment.getStatus() != currentStatus){
            payment.setStatus(currentStatus);
            paymentRepository.save(payment);
        }

        return currentStatus;
    }

    @Override
    public PaymentProviderType getProviderType() {
        return null;
    }

    @Override
    public String createPaymentRecord(PaymentRequest paymentRequest) {
        log.info("Creating a Payment record");

        Payment payment = new Payment();
        payment.setOrderId(paymentRequest.getOrderId());
        payment.setAmount(paymentRequest.getAmount());
        payment.setCurrency(paymentRequest.getCurrency());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        return payment.getId();
    }

    @Override
    public void updatePaymentRecord(PaymentUpdateRequest request) {
        log.info("Updating a payment record");

        Payment payment = paymentRepository.findById(request.getId());
        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

    }

    @Override
    public void deletePaymentRecord(String paymentId) {
        log.info("Deleting a payment record");

        Payment payment = paymentRepository.findById(paymentId);

        if (payment != null){
            paymentRepository.delete(payment);
        } else {
            log.error("Payment record not found");
        }
    }

    private boolean canRefund(Payment payment){
        return payment.getStatus() == PaymentStatus.COMPLETED ||
                payment.getStatus() == PaymentStatus.SETTLED;
    }

    private PaymentStatus mapGatewayStatus(PaymentGatewayStatus gatewayStatus){
        return switch (gatewayStatus){
            case SUCCESSFUL -> PaymentStatus.COMPLETED;
            case PENDING -> PaymentStatus.PENDING;
            case FAILED -> PaymentStatus.FAILED;
            case REFUNDED -> PaymentStatus.REFUNDED;
            default -> PaymentStatus.UNKNOWN;
        };
    }

    private void validatePaymentRequest(PaymentRequest request){
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new PaymentValidationException("Invalid payment amount");
        }
        if(request.getOrderId() == null || request.getOrderId().isEmpty()){
            throw new PaymentValidationException("Order ID is required");
        }
    }
}
