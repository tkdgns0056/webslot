package com.example.slotweb.service.pay;

import com.example.slotweb.service.pay.dto.Pay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PayMapper {
    List<Pay> selectPayList(String memberId);

    void deletePay(@Param("list") List<Long> deletePayIds);

    void savePay(@Param("list") List<Pay> savePays);

    List<Pay> selectPayList2(@Param("list") List<String> deletePayIds);

    int selectPayCount(@Param("memberId") String memberId);

    int selectDsPayCount(@Param("memberId") String memberId);

    Integer selectAdminPayCount();

    void deletePayByMemberId(@Param("list") List<String> deleteMemberIds);

    List<Map<String, Object>> selectPayTypeCount(@Param("memberId") String memberId);

    List<Map<String, Object>> selectDsPayTypeCount(@Param("memberId") String memberId);

    List<Map<String, Object>> selectAdminPayTypeCount();

    List<Pay> selectPayInfoList(String memberId);

    List<Pay> selectDsMemberPayInfoList(@Param("memberId") String memberId);

    List<Pay> selectAdminMemberPayInfoList();

    void updateEndDt(@Param("list") List<Integer> extendPayNos, @Param("extendDay") Integer extendDay);
}
