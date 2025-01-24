package com.bang.WebBanHang_Project.controller.request;

import com.bang.WebBanHang_Project.common.Gender;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@ToString
@Setter
public class UserUpdateRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Date birthday;
    private String username;
    private String email;
    private String phone;
    private List<AddressRequest> addresses;
}
