package com.example.setty.service;

import com.example.setty.entity.Member;
import com.example.setty.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional(readOnly = true) // 읽기 전용 트랜잭션 (데이터베이스 데이터 읽기)
//@RequiredArgsConstructor //final 필드에 대한 생성자를 자동으로 만들어 줌 -> memberRepository, encoder
public class MemberServiceImple implements MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    public MemberServiceImple(MemberRepository memberRepository, BCryptPasswordEncoder encoder) {
        this.memberRepository = memberRepository;
        this.encoder = encoder;
    }

    @Transactional //readOnly=true가 아닌 쓰기 작업 필요하므로 별도 어노테이션.
    @Override
    public Long join(Member member) {
        Optional<Member> vailMember = memberRepository.findByEmail(member.getEmail()); //optional로 null 안정성 / 이메일로 회원 조회
        if(vailMember.isPresent()) { //isPresent(): 값이 존재하는지 확인. 같은 이메일이 이미 존재하면 true
            //throw new ValidateMemberException("This member email is already exist. "+member.getEmail()); 커스텀 예외!
            throw new IllegalArgumentException("This member email is already exist. "+member.getEmail()); //표준 예외: 잘못된 인자가 넘어갔을 때 발생
        }

        member.encodePassword(encoder.encode(member.getPassword())); // 암호를 암호화하는 코드

        memberRepository.create(member); //DB에 회원 정보 저장.
        return member.getMemberId(); //회원가입 완료되면 회원 ID 반환.
    }

}
