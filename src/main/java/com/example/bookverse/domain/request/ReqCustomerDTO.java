package com.example.bookverse.domain.request;

import java.util.Optional;

import com.example.bookverse.domain.enums.CustomerLevel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReqCustomerDTO {
    private Optional<Long> id;
    private String identityCard;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String address;
    private String phone;
    private String avatar;
    private CustomerLevel customerLevel;
}
