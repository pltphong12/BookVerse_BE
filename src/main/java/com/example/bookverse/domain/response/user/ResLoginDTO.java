package com.example.bookverse.domain.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResLoginDTO {
    private UserDTO user;
    private String accessToken;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInSideToken {
        private long id;
        private String username;
        private String fullName;
        private String role;
    }
}

