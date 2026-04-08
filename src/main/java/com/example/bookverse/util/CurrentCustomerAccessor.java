package com.example.bookverse.util;

import org.springframework.stereotype.Component;

import com.example.bookverse.domain.Customer;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.repository.CustomerRepository;

@Component
public class CurrentCustomerAccessor {

    private final CustomerRepository customerRepository;

    public CurrentCustomerAccessor(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Lấy {@link Customer} từ {@code customerId} trong access token, đối chiếu email (subject) để tránh lệch phiên.
     */
    public Customer requireCurrentCustomer() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new IdInvalidException("Bạn chưa đăng nhập"));
        long customerId = SecurityUtil.getCurrentCustomerId()
                .orElseThrow(() -> new IdInvalidException(
                        "Token không có thông tin khách hàng — vui lòng đăng nhập lại để cập nhật token"));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy khách hàng"));
        if (customer.getUser() == null || !email.equalsIgnoreCase(customer.getUser().getEmail())) {
            throw new IdInvalidException("Phiên đăng nhập không khớp với tài khoản khách hàng");
        }
        return customer;
    }
}
