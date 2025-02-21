package com.bang.WebBanHang_Project.controller.response;

import com.bang.WebBanHang_Project.common.RefundStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RefundGatewayResponse implements SignedResponse {
    private String refundId;
    private RefundStatus status;
    private String message;
    private String signature;
}
