package com.example.bookverse.domain.response.customer;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.bookverse.domain.Customer;
import com.example.bookverse.domain.User;
import com.example.bookverse.domain.enums.CustomerLevel;
import com.example.bookverse.domain.response.user.UserDTO;

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
    private String identityCard;
    private Long totalOrder;
    private BigDecimal totalSpending;
    private String customerLevel;
    private UserDTO user;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    public static ResCustomerDTO fromCustomer(Customer customer, User user) {
        ResCustomerDTO resCustomerDTO = new ResCustomerDTO();
        resCustomerDTO.setId(customer.getId());
        resCustomerDTO.setIdentityCard(customer.getIdentityCard());
        resCustomerDTO.setTotalOrder(customer.getTotalOrder());
        resCustomerDTO.setTotalSpending(customer.getTotalSpending());
        resCustomerDTO.setCustomerLevel(customer.getCustomerLevel().getDisplayName());
        resCustomerDTO.setCreatedAt(customer.getCreatedAt());
        resCustomerDTO.setUpdatedAt(customer.getUpdatedAt());
        resCustomerDTO.setCreatedBy(customer.getCreatedBy());
        resCustomerDTO.setUpdatedBy(customer.getUpdatedBy());
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setPhone(user.getPhone());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setRole(user.getRole());
        resCustomerDTO.setUser(userDTO);
        return resCustomerDTO;
    }
}
