package com.example.jpa.repository;

import com.example.jpa.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findMemberByName(String name);
    Member findByPhone(String phone);
    List<Member> findByAge(int age);
    Member findByNameAndAddress(String name, String address);
    List<Member> findByAgeGreaterThanEqual(int age);
}
