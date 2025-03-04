package com.bang.WebBanHang_Project.controller.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class OrderPageResponse extends PageResponseAbstract implements Serializable {
    private List<OrderResponse> orders;
}
