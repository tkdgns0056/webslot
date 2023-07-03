package com.example.slotweb.service.member;

import com.example.slotweb.service.member.dto.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
    Member selectMember(String memberId);
    List<Member> selectMemberList(@Param("searchText")  String searchText, @Param("managerId") String managerId);

    void deleteMember(@Param("list") List<String> deleteMemberIds);

    void saveMember(@Param("list") List<Member> saveMembers);

    int checkDupMember(@Param("list") List<String> saveMemberIds);

    Member selectDsMember(@Param("memberId") String memberId);
}
