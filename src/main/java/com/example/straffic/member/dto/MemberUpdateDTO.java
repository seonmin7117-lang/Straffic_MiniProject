package com.example.straffic.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberUpdateDTO {
    private String id;
    private String pw;
    private String name;
    private String tel;
}
