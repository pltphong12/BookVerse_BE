package com.example.bookverse.domain.response.customer;

import java.time.Instant;

import com.example.bookverse.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResCustomerDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String address;
    private String phone;
    private String avatar;
    private Long totalOrder;
    private Long totalSpending;
    private Role role;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}
