package com.example.slotweb.service.slot;

import com.example.slotweb.service.slot.dto.Slot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SlotMapper {
    List<Slot> selectSlotList(@Param("memberId") String memberId, @Param("searchText") String searchText, @Param("searchType") String searchType);

    List<Slot> selectAdminSlotList(@Param("searchText") String searchText, @Param("searchType")  String searchType);

    List<Slot> selectDsSlotList(@Param("memberId") String memberId, @Param("searchText") String searchText, @Param("searchType")  String searchType);

    void deleteSlot(@Param("list") List<Long> deleteSlotNos);

    void saveSlot(@Param("list") List<Slot> saveSlots);

    Integer selectSlotCount(@Param("memberId") String memberId);

    Integer selectDsSlotCount(@Param("memberId") String memberId);

    Integer selectAdminSlotCount();

    void deleteSlotByMemberId(@Param("list") List<String> deleteMemberIds);

    void deleteSlotByPayNo(List<Long> deletePayIds);

    List<Map<String, Long>> selectTypeUsedSlot(@Param("memberId") String memberId);

    void deleteBatchSlot();
}
