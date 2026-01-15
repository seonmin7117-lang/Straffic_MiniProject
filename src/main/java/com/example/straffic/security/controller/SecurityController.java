package com.example.straffic.security.controller;

import com.example.straffic.member.entity.MemberEntity;
import com.example.straffic.member.repository.MemberRepository;
import com.example.straffic.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SecurityController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @GetMapping("/admin/security")
    public String securityAdminRedirect() {
        return "redirect:/dashboard/security";
    }

    @PostMapping("/security/member/role")
    public String updateRole(@RequestParam("memberId") String memberId,
                             @RequestParam("role") String role) {
        memberService.updateRole(memberId, role);
        return "redirect:/dashboard/security";
    }

    @PostMapping("/security/member/delete")
    public String deleteMember(@RequestParam("memberId") String memberId) {
        memberService.deleteByAdmin(memberId);
        return "redirect:/dashboard/security";
    }
}
