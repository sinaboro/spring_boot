package com.example.jpa.domain.service;

import com.example.jpa.domain.Member;
import com.example.jpa.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Log4j2
public class MemberService {

    private MemberRepository memberRepository;

    //생성자 주입
    public MemberService(MemberRepository memberRepository){
        this.memberRepository  = memberRepository;
    }

    //수정
    public void update(Member member){
        memberRepository.save(member);
    }

    //추가
    public void  insert(Member member){
        memberRepository.save(member);
    }

    //삭제
    public void delete(int memberId){
        memberRepository.deleteById(memberId);
    }

    //조회
    public Member findById(int memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원이 없습니다."));

        return  member;
    }

    //전제 데이타 조회
    public List<Member> findByALL(){
        return memberRepository.findAll();
    }
}
