package com.bang.WebBanHang_Project.service;

import com.bang.WebBanHang_Project.controller.request.UserCreationRequest;
import com.bang.WebBanHang_Project.controller.request.UserPasswordRequest;
import com.bang.WebBanHang_Project.controller.request.UserUpdateRequest;
import com.bang.WebBanHang_Project.controller.response.UserPageResponse;
import com.bang.WebBanHang_Project.controller.response.UserResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserPageResponse findAll(String keyword, String sort, int page, int size);

    UserPageResponse findAllWithCriteria(int page, int size, String sortBy, String address ,String... search);

    UserPageResponse findAllWithSpecification(Pageable pageable, String[] user, String[] address);

    UserResponse findById(Long id);

    UserResponse findByUsername(String username);

    UserResponse findByEmail(String email);

    long save(UserCreationRequest req);

    void update(UserUpdateRequest req);

    void changePassword(UserPasswordRequest req);

    void delete(Long id);

}
