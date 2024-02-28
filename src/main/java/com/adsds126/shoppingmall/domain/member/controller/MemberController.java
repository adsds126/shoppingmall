package com.adsds126.shoppingmall.domain.member.controller;


import com.adsds126.shoppingmall.domain.member.dto.MemberDto;
import com.adsds126.shoppingmall.domain.member.entity.Member;
import com.adsds126.shoppingmall.domain.member.mapper.MemberMapper;
import com.adsds126.shoppingmall.domain.member.service.MemberService;
import com.adsds126.shoppingmall.response.ApiResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;

    /**
     * 회원가입 api : 이메일로 랜덤키발송
     * param = email, pw, address
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody MemberDto.Signup requestBody) {
        String subject = "회원가입 이메일인증 랜덤키를 발송합니다.";
        //member 객체를 만들고, signup 메서드로 DB에 저장.
        Member member = memberService.signup(memberMapper.memberDtoSignupToMember(requestBody));
        //입력받은 이메일, subject로 이메일템플릿에 randomKey를 담아 보낸다.
        try {
            memberService.sendEmail(member.getEmail(), subject);
            return ResponseEntity.ok("이메일이 성공적으로 발송되었습니다.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 발송에 실패했습니다.");
        }
    }

    /**
     * 인증번호 확인 api : 이메일로받은 랜덤키 검증후, emailVerifiedYn = true로.
     * param = randomKey, email
     * @return = message "이메일 인증이 완료되었습니다."
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody MemberDto.Verify requestBody) {
        try{
            memberService.verifyRandomKey(requestBody.getRandomKey(), requestBody.getEmail());
            return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 처리중 오류가 발생했습니다.");
        }
    }

    /**
     * 회원정보 불러오기
     * param = memberId
     * @return memberId, email, roleType, address, modifiedAt, createdAt
     *
     */
    @GetMapping("/{member-id}")
    public ResponseEntity<?> getMember(@PathVariable("member-id") Long memberId) {
        Member member = memberService.findMember(memberId);
        MemberDto.Response response = memberMapper.memberToMemberDtoResponse(member);
        return ResponseEntity.ok().body(ApiResponse.ok("data",response));
    }

    /**
     * 비밀번호,이메일 제외 회원정보 수정
     * param = address
     * @return = memberId, email, roleType, address, modifiedAt, createdAt
     */
    @PatchMapping("/{member-id}")
    public ResponseEntity<?> updateMember(@PathVariable("member-id") Long memberId, @RequestBody MemberDto.Update requestBody) {
        memberService.updateMember(memberId, requestBody.getAddress());
        Member member = memberService.findMember(memberId);
        MemberDto.Response response = memberMapper.memberToMemberDtoResponse(member);
        return ResponseEntity.ok().body(ApiResponse.ok("data",response));
    }

    /**
     * 회원삭제
     * param = memberId
     * @return = message "회원삭제가 완료되었습니다."
     */
    @DeleteMapping("/{member-id}")
    public ResponseEntity<?> deleteMember(@PathVariable("member-id") Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.ok("회원삭제가 완료되었습니다.");
    }

    /**
     * 로그인
     * param = email, password
     * @return = 리프레시 토큰
     */
    @PostMapping("/login")
    public ResponseEntity<?> login() {

        return null;
    }

    /**
     * 리프레시 토큰 재발급
     * param = refreshToken
     * @return = newToken
     */
    @GetMapping("/refresh")
    public ResponseEntity<?> updateRefreshToken() {
        return null;
    }

    /**
     * 비밀번호 찾기 인증번호 전송
     * param = email, subject
     * @return = token
     */
    @PostMapping("/password")
    public ResponseEntity<?> findPw(@RequestBody MemberDto.PasswordFind requestBody) {
        String subject = "비밀번호 찾기용 랜덤키를 발송합니다.";
        try {
            memberService.sendEmail(requestBody.getEmail(), subject);
            return ResponseEntity.ok("이메일이 성공적으로 발송되었습니다.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 발송에 실패했습니다.");
        }
    }

    /**
     * 비밀번호 찾기 인증번호검증,이메일로 비밀번호 전송
     * param = email, randomKey
     * @return = 이메일전송
     */
    @PostMapping("/confirm-pw")
    public ResponseEntity<?> confirmFindPw(@RequestBody MemberDto.Verify requestBody) {
        try{
            memberService.verifyAndSend(requestBody.getRandomKey(), requestBody.getEmail());
            return ResponseEntity.ok("이메일로 임시비밀번호를 전송했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 처리중 오류가 발생했습니다.");
        }
    }


}
