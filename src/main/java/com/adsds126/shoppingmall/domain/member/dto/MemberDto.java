package com.adsds126.shoppingmall.domain.member.dto;

import com.adsds126.shoppingmall.domain.member.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
public class MemberDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Signup {
        @NotNull
        private String email;

        @NotNull
        private String password;

        @NotNull
        private String address;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update {
        @NotNull
        private String address;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long memberId;
        private String email;
        private RoleType roleType;
        private String address;
        private LocalDateTime modifiedAt;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Login {
        @NotNull
        private String email;

        @NotNull
        private String password;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class RefreshToken {
        @NotNull
        private String refreshToken;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PasswordFind {
        @NotNull
        private String email;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Verify {
        @NotNull
        private String randomKey;

        @NotNull
        private String email;
    }

}
