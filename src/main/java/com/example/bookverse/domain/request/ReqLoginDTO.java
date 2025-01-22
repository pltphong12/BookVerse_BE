package com.example.bookverse.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReqLoginDTO {
    @NotBlank(message = "username isn't blank")
    private String username;
    @NotBlank(message = "password isn't blank")
    private String password;
}

