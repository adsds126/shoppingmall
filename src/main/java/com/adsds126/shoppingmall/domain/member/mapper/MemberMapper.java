package com.adsds126.shoppingmall.domain.member.mapper;

import com.adsds126.shoppingmall.domain.member.dto.MemberDto;
import com.adsds126.shoppingmall.domain.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    @Mapping(target = "memberId", ignore = true)
    Member memberDtoSignupToMember(MemberDto.Signup requestBody);
    MemberDto.Response memberToMemberDtoResponse(Member member);
}
