package com.adsds126.shoppingmall.domain.member.repository;

import com.adsds126.shoppingmall.domain.member.entity.EmailConfirmRandomKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface EmailConfirmRandomKeyRepository extends JpaRepository<EmailConfirmRandomKey, String> {
    Optional<EmailConfirmRandomKey> findByEmail(String email);
}
