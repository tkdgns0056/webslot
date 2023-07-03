package com.example.slotweb.service.excel;

import com.example.slotweb.service.slot.dto.Slot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.ResultHandler;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExcelMapper {

    void selectPcList(Map params, ResultHandler<Map<String, Object>> resultHandler);

    void selectMobileList(Map params, ResultHandler<Map<String, Object>> resultHandler);

    void selectCalcList(Map params, ResultHandler<Map<String, Object>> resultHandler);

    void saveExcelSlot(List<Slot> slots);
}
