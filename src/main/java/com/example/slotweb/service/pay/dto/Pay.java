package com.example.slotweb.service.pay.dto;

import com.example.slotweb.service.member.dto.Member;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Alias("Pay")
@Getter @Setter
public class Pay {
    private Long payNo;        // 결제번호
    private String memberId;        // 회원
    private String managerId;        // 회원
    private LocalDate startDt;        // 시작일
    private LocalDate endDt;        // 종료일
    private String type; // 카워드 타입
    private String memo;        // 종료일
    private Long slotCnt;        // 슬롯개수
    private String delYn;
    private String delType; // D : 삭제, E : 만료, N : 활성화
    private Integer extendDay; // 연장일

    @Getter
    @Setter
    public static class Save {
        private List<Pay> savePays;
        List<Long> deletePayIds;
    }

    @Getter
    @Setter
    public static class Extend {
        private List<Integer> extendPayNos;
        private Integer extendDay;
    }
}
