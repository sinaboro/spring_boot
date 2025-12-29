package com.example.demo.mapper;

import com.example.demo.domain.MemberDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
//@Transactional //테스트 종료 후 rollback
class MemberMapperTest {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    @DisplayName("ALL READ: 전체 데이타 조회")
    void findAll() {
        // 이 상항에서 -> 이런 행동을하면 -> 이런 결과가 나와야한다.

        //given -> 테스트에 필요한 모든 조건을 만드는 단계

        //when -> 테스트 대상 동작 실행(실제로 검증하고 싶은 행동을 수행하는 단계)
        List<MemberDTO> list = memberMapper.findAll();

        //then -> 검증 결과 단계
        Assertions.assertNotNull(list);  //null만 아니면 OK

        list.forEach( m -> log.info(m));

    }
}