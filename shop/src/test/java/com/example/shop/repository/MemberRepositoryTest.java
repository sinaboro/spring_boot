package com.example.shop.repository;

import com.example.shop.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "gildong", roles = "USER")
    public void auditingTest(){

        Member member = new Member();

        memberRepository.save(member);
    }

    @Test
    @DisplayName("gildong 수정")
    @WithMockUser(username = "hong", roles = "USER")
    public void updateTest(){
        Member member = memberRepository.findById(2L)
                .orElseThrow(EntityNotFoundException::new);

        member.setName("까미");

        memberRepository.save(member);
    }
}



