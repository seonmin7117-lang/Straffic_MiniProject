package com.example.straffic.member.controller;

import com.example.straffic.member.dto.MemberCreateDTO;
import com.example.straffic.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberCreateController {

    private final MemberService memberService;

    @GetMapping("/memberInput")
    public String memberInput(Model model) {
        model.addAttribute("memberDTO", new MemberCreateDTO());
        return "member/memberInput";
    }

    @PostMapping("/memInsert")
    public String memberInsert(@ModelAttribute("memberDTO") @Valid MemberCreateDTO memberDTO,
                               BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "member/memberInput";
        } else {
            log.info("회원가입 처리 시작");
            try {
                memberService.memberinsert(memberDTO, response);
                return "redirect:/";
            } catch (IllegalStateException e) {
                bindingResult.rejectValue("id", "duplicate", "이미 가입된 아이디입니다");
                return "member/memberInput";
            }
        }
    }
}
