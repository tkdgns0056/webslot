package com.example.slotweb.service.member;

import com.example.slotweb.security.LoginMember;
import com.example.slotweb.service.member.dto.Member;
import com.example.slotweb.service.pay.PayService;
import com.example.slotweb.service.slot.SlotService;
import com.example.slotweb.utils.LoginUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberMapper memberMapper;
    private final SlotService slotService;
    private final PayService payService;


    @Override
    public UserDetails loadUserByUsername(String memberId) {
        Member member = memberMapper.selectMember(memberId);
        return member != null ? new LoginMember(member) : null;
    }

    /**
     * 페이징 처리
     * @param pageNum
     * @param pageSize
     * @param searchText
     * @return
     */
    public PageInfo<Member> getMembersWithPaging(int pageNum, int pageSize, String searchText) {
        Page<Member> page = PageHelper.startPage(pageNum, pageSize);

        String managerId = null;
        if ("DS".equals(LoginUtils.getMember().getRoleId())) {
            managerId = LoginUtils.getMemberId();
        }
        List<Member> members = memberMapper.selectMemberList(searchText, managerId);

        return new PageInfo<>(members, page.getPages());
    }

//    public List<Member> getMemberList(String searchText) {
//        String managerId = null;
//        if( "DS".equals(LoginUtils.getMember().getRoleId())){
//            managerId = LoginUtils.getMemberId();
//        }
//
//        return memberMapper.selectMemberList(searchText, managerId);
//    }

    @Transactional
    public void saveMember(List<Member> saveMembers, List<String> deleteMemberIds) {

        if( deleteMemberIds != null && !deleteMemberIds.isEmpty()){
            // 회원 삭제
            memberMapper.deleteMember(deleteMemberIds);
            // 슬롯삭제
            slotService.deleteSlotByMemberId(deleteMemberIds);
            // 결제 삭제
            payService.deletePayByMemberId(deleteMemberIds);
        }

        if( saveMembers != null && !saveMembers.isEmpty()){

            List<String> newMemberIds = saveMembers.stream().filter( e-> "new".equals( e.getType())).map( Member::getMemberId).collect(Collectors.toList());

            if( !newMemberIds.isEmpty() && checkDupMember(newMemberIds)){
                throw new RuntimeException("이미 가입된 아이디가 존재합니다.");
            }

            saveMembers.stream().forEach( e -> {
                e.setManagerId(LoginUtils.getMemberId());
            });

            // 회원 등록/수정
            memberMapper.saveMember(saveMembers);
        }

    }

    public boolean checkDupMember(List<String> saveMemberIds) {

        return memberMapper.checkDupMember(saveMemberIds) == 1 ? true : false;
    }

    public Member getDsMember(String memberId) {
        return memberMapper.selectDsMember(memberId);
    }
}
