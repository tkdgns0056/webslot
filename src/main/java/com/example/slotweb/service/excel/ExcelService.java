package com.example.slotweb.service.excel;

import com.example.slotweb.extend.ExcelContext;
import com.example.slotweb.extend.ExcelSupport;
import com.example.slotweb.service.member.MemberService;
import com.example.slotweb.service.member.dto.Member;
import com.example.slotweb.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExcelService implements ExcelSupport {


    final private ExcelMapper excelMapper;
    final private MemberService memberService;

    @Override
    public void buildExcelData(String uid, Map params, ExcelContext excelContext) {

        if("PC".equals(uid)){
            excelMapper.selectPcList(params, new ResultHandler<Map<String, Object>>() {

                int index = excelContext.getSheetLastRowNum(ExcelContext.FIRST_SHEET_IDX);
                Sheet sheet = excelContext.getSheet(ExcelContext.FIRST_SHEET_IDX);

                @Override
                public void handleResult(ResultContext<? extends Map<String, Object>> resultContext) {

                    Map<String, Object> resultObj = resultContext.getResultObject();
                    Row row = sheet.createRow(++index);
                    excelContext.renderData(
                            row
                            , "기본그룹"
                            , resultObj.get("keyword")
                            , resultObj.get("product_name")
                            , resultObj.get("category_no")
                            , resultObj.get("mid")
                            , resultObj.get("sub_keyword")
                            , ""
                            , resultObj.get("memo")
                    );
                }
            });
        }else if("MOBILE".equals(uid)){


            excelMapper.selectMobileList(params, new ResultHandler<Map<String, Object>>() {

                int index = excelContext.getSheetLastRowNum(ExcelContext.FIRST_SHEET_IDX);
                Sheet sheet = excelContext.getSheet(ExcelContext.FIRST_SHEET_IDX);

                @Override
                public void handleResult(ResultContext<? extends Map<String, Object>> resultContext) {

                    Map<String, Object> resultObj = resultContext.getResultObject();
                    Row row = sheet.createRow(++index);
                    excelContext.renderData(
                            row
                            , "모바일"
                            , resultObj.get("keyword")
                            , resultObj.get("category_no")
                            , resultObj.get("mid")
                            , resultObj.get("ms")
                            , resultObj.get("memo")
                    );
                }
            });

        }else if ("CALC".equals(uid)){
            if(ObjectUtils.isEmpty(params.get("managerId"))){
                throw new RuntimeException("총판아이디를 입력하세요.");
            }

            String managerId = (String)params.get("managerId");
            Member member = memberService.getDsMember(managerId);

            if( member == null){
                throw new RuntimeException("존재하지 총판 아이디 입니다.");
            }

            excelMapper.selectCalcList(params, new ResultHandler<Map<String, Object>>() {

                int index = excelContext.getSheetLastRowNum(ExcelContext.FIRST_SHEET_IDX);
                Sheet sheet = excelContext.getSheet(ExcelContext.FIRST_SHEET_IDX);

                @Override
                public void handleResult(ResultContext<? extends Map<String, Object>> resultContext) {

                    Map<String, Object> resultObj = resultContext.getResultObject();
                    Row row = sheet.createRow(++index);
                    excelContext.renderData(
                            row
                            , resultObj.get("manager_id")
                            , resultObj.get("member_id")
                            , resultObj.get("start_dt")
                            , resultObj.get("end_dt")
                            , resultObj.get("slot_cnt")
                            , "미구현"
                            , resultObj.get("status")
                    );
                }
            });
        }


    }
}
