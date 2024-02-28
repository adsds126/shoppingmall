package com.adsds126.shoppingmall.domain.member.repository;

import com.adsds126.shoppingmall.domain.member.entity.MemberRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, Long> {
}
