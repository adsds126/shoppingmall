package com.adsds126.shoppingmall.domain.member.service;

import com.adsds126.shoppingmall.domain.member.RoleType;
import com.adsds126.shoppingmall.domain.member.entity.EmailConfirmRandomKey;
import com.adsds126.shoppingmall.domain.member.entity.Member;
import com.adsds126.shoppingmall.domain.member.repository.EmailConfirmRandomKeyRepository;
import com.adsds126.shoppingmall.domain.member.repository.MemberRepository;
import com.adsds126.shoppingmall.exception.BusinessLogicException;
import com.adsds126.shoppingmall.exception.ExceptionCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final EmailConfirmRandomKeyRepository emailConfirmRandomKeyRepository;
    private final TemplateEngine templateEngine;


    public Member signup(Member member) {
        //이미 가입된 email인지 검증
        verifyExistsUserEmail(member.getEmail());
        //비밀번호는 암호화해서 저장
        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        //builder 로 새로운 회원객체 생성
        Member newMember = Member.builder()
                .password(encryptedPassword)
                .email(member.getEmail())
                .roleType(RoleType.USER)
                .address(member.getAddress())
                .emailVerifiedYn(false)
                .modifiedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        //새로운 회원 객체 DB에 저장.
        return memberRepository.save(newMember);
    }

    public void sendEmail(String recipientEmail, String subject) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            String randomKey = generateRandomKey(7);
            // EmailConfirmRandomKey 저장
            EmailConfirmRandomKey confirmRandomKey = EmailConfirmRandomKey.builder()
                    .email(recipientEmail)
                    .randomKey(randomKey)
                    .build();
            emailConfirmRandomKeyRepository.save(confirmRandomKey);
            // 이메일 템플릿 불러오기
            String emailTemplate = getEmailTemplate(recipientEmail, randomKey);
            helper.setText(emailTemplate, true);

            javaMailSender.send(message);
            System.out.println("Email Template: " + emailTemplate);
        } catch (MessagingException e) {
            // 예외 처리 로직 작성
            e.printStackTrace(); // 예외 내용을 콘솔에 출력하거나 로깅할 수 있습니다.
            // 예외 처리 후 필요한 작업 수행
        }
    }

    public void verifyRandomKey(String randomKey, String email) {
        EmailConfirmRandomKey emailConfirmRandomKey = emailConfirmRandomKeyRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 이메일입니다."));
        String newRandomKey = emailConfirmRandomKey.getRandomKey();

        if(!randomKey.equals(newRandomKey)){
            throw new IllegalArgumentException("인증코드가 유효하지 않습니다.");
        }
        Optional<Member> existingMember = memberRepository.findByEmail(email);
        if(existingMember.isPresent()){
            Member member = existingMember.get();
            member.setEmailVerifiedYn(true);
            memberRepository.save(member);
        }
        emailConfirmRandomKeyRepository.deleteById(email);
    }

    public void verifyAndSend(String randomKey, String email) {
        EmailConfirmRandomKey emailConfirmRandomKey = emailConfirmRandomKeyRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 이메일입니다."));
        String newRandomKey = emailConfirmRandomKey.getRandomKey();

        if(!randomKey.equals(newRandomKey)){
            throw new IllegalArgumentException("인증코드가 유효하지 않습니다.");
        }
        Optional<Member> existingMember = memberRepository.findByEmail(email);
        String tempPw = generateTempPw(10);
        if(existingMember.isPresent()){
            Member member = existingMember.get();
            String encodedTempPw = passwordEncoder.encode(tempPw);
            member.setPassword(encodedTempPw);
            memberRepository.save(member);
            try {
                String subject = "[shopppingmall] 임시 비밀번호를 보내드립니다.";
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(member.getEmail());
                helper.setSubject(subject);
                // 이메일 템플릿 불러오기
                String pwTemplate = getPasswordTemplate(member.getEmail(), tempPw);
                helper.setText(pwTemplate, true);

                javaMailSender.send(message);
                System.out.println("pw_Template: " + pwTemplate);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        emailConfirmRandomKeyRepository.deleteById(email);
    }

    public Member findMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);

        return member.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
    }

    public void updateMember(Long memberId, String newAddress) {
        Member existMember = memberRepository.findById(memberId)
                .orElseThrow(()-> new EntityNotFoundException("사용차를 찾을 수 없습니다."));
        existMember.setAddress(newAddress);
        memberRepository.save(existMember);
    }

    public void deleteMember(Long memberId) {
        Member existMember = memberRepository.findById(memberId)
                .orElseThrow(()-> new EntityNotFoundException("사용차를 찾을 수 없습니다."));
        memberRepository.delete(existMember);
    }
    public String generateTempPw(int keyLength) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%";
        StringBuilder randomKey = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < keyLength; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            randomKey.append(characters.charAt(randomIndex));
        }

        return randomKey.toString();
    }
    public String generateRandomKey(int keyLength) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomKey = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < keyLength; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            randomKey.append(characters.charAt(randomIndex));
        }

        return randomKey.toString();
    }



    @Transactional(readOnly = true)
    public void verifyExistsUserEmail(String email) {
        memberRepository.findByEmail(email).ifPresent((e) -> {
            throw new BusinessLogicException(ExceptionCode.ALREADY_EXISTS_EMAIL);
        });
    }

    public String getEmailTemplate(String recipientEmail, String randomKey) {
        try {
            // Create the Thymeleaf context and set variables
            Context context = new Context();
            context.setVariable("recipientEmail", recipientEmail);
            context.setVariable("randomKey", randomKey);

            // Process the email template using the template engine
            String emailTemplate = templateEngine.process("email_template", context);

            return emailTemplate;
        } catch (Exception e) {
            throw new RuntimeException("Failed to process email template.", e);
        }
    }
    public String getPasswordTemplate(String recipientEmail, String tempPw) {
        try {
            // Create the Thymeleaf context and set variables
            Context context = new Context();
            context.setVariable("recipientEmail", recipientEmail);
            context.setVariable("tempPw", tempPw);

            // Process the email template using the template engine
            String emailTemplate = templateEngine.process("pw_template", context);

            return emailTemplate;
        } catch (Exception e) {
            throw new RuntimeException("Failed to process email template.", e);
        }
    }
}
