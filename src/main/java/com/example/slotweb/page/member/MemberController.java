package com.example.slotweb.page.member;

import com.example.slotweb.service.member.MemberService;
import com.example.slotweb.service.member.dto.Member;
import com.example.slotweb.service.slot.SlotService;
import com.example.slotweb.service.slot.dto.Slot;
import com.example.slotweb.utils.LoginUtils;
import com.example.slotweb.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/list")
    public String listMember(Model model,
                             @RequestParam(defaultValue = "1") int pageNum,
                             @RequestParam(defaultValue = "10") int pageSize,
                             @RequestParam(required = false) String searchText) {

        // 페이징 관련 처리
        PageInfo<Member> pageInfo = memberService.getMembersWithPaging(pageNum, pageSize, searchText);

        // searchText가 null인 경우 빈 문자열로 설정
        if (searchText == null) {
            searchText = "";
        }
        model.addAttribute("searchText", searchText);
        model.addAttribute("pageInfo", pageInfo);

        return "member/list";
    }

    @PostMapping("/member/save")
    @ResponseBody
    public ResponseEntity<?> saveMember(@RequestBody Member.Save saveMember) {

        try {
            memberService.saveMember(saveMember.getSaveMembers(), saveMember.getDeleteMemberIds());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
