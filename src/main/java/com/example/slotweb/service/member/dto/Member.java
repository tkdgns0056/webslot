package com.example.slotweb.service.member.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias("Member")
@Getter @Setter
public class Member {
    private String memberId;        // 회원아이디
    private String password;        // 패스워드
    private String managerId;        // 총판아이디
    private String roleId;        // 권한아이디
    private String useYn;        // 사용여부
    private String memo;        // 메모
    private Integer slotTotalCnt; // 총슬롯 개수
    private String type;

    @Getter
    @Setter
    public static class Save {
        private List<Member> saveMembers;
        List<String> deleteMemberIds;
    }
}
