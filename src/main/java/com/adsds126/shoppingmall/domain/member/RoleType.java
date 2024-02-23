package com.adsds126.shoppingmall.domain.member;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoleType {
    USER("ROLE_USER", "일반 사용자 권한"),
    ADMIN("ROLE_ADMIN", "관리자 권한");

    private final String code;
    private final String displayName;
}
