package com.example.bookverse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResLoginDTO {
    private ResUserDTO user;
    private String accessToken;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInSideToken {
        private long id;
        private String email;
        private String fullName;
        private String role;
    }
}

