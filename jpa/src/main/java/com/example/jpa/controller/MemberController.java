package com.example.jpa.controller;

import com.example.jpa.domain.Member;
import com.example.jpa.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/list")
    public void getList(Model model){
        List<Member> memberList = memberService.findByALL();
        model.addAttribute("memberList", memberList);
    }

    @GetMapping("/new")
    public void getNew(){

    }


}
