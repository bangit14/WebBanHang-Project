package com.bang.WebBanHang_Project.repository;

import com.bang.WebBanHang_Project.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Payment findById(String paymentId);

}
