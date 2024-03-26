package com.adsds126.shoppingmall.domain.member.repository;

import com.adsds126.shoppingmall.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findById(Long memberId);
    List<Member> findMembersByEmail(String email);
    Member findMemberByEmail(String email);

    Optional<Member> findOneWithAuthoritiesByEmail(String email);
}
