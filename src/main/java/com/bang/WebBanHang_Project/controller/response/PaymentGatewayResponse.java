package com.bang.WebBanHang_Project.controller.response;

import com.bang.WebBanHang_Project.common.PaymentGatewayStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentGatewayResponse implements SignedResponse {

    private String transactionId;
    private PaymentGatewayStatus status;
    private String message;
    private String signature;

    @Override
    public String getSignature() {
        return "";
    }

    @Override
    public void setSignature(String signature) {

    }
}
