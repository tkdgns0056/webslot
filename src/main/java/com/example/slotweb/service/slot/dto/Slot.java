package com.example.slotweb.service.slot.dto;

import com.example.slotweb.service.pay.dto.Pay;
import com.example.slotweb.utils.LoginUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Alias("Slot")
@Setter @Getter
public class Slot {

    private Long slotNo;        // 슬롯번호
    private Long payNo;        // 슬롯번호
    private String memberId;        // 회원아이디
    private String productName;        // 상품명
    private String type;        // 검색키워드 타입
    private String keyword;        // 검색키워드
    private String subKeyword;        // 서브키워드
    private Long categoryNo;        // 카테고리번호
    private Long mid;        // MID값
    private Long ms;        // MS값
    private String memo;        // 메모
    private LocalDate expireDt; // 만료일

    @Getter
    @Setter
    public static class Save {
        private List<Slot> saveSlots;
        List<Long> deleteSlotNos;
    }
}
