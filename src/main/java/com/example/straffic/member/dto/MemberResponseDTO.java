package com.example.straffic.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberResponseDTO {
    private String id;
    private String name;
    private String tel;
}
