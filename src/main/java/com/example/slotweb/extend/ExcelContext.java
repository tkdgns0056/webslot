package com.example.slotweb.extend;

import com.example.slotweb.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ExcelContext {

    /**
     * 첫번째 시트 인덱스
     */
    public static final int FIRST_SHEET_IDX = 0;

    /**
     * 두번째 시트 인덱스
     */
    public static final int SECOND_SHEET_IDX = 1;

    private XSSFWorkbook xssfWorkbook;
    private SXSSFWorkbook sxssfWorkbook;


    public Sheet getSheet(int index) {
        return sxssfWorkbook.getSheetAt(index);
    }

    public int getSheetLastRowNum(int index) {
        return xssfWorkbook.getSheetAt(index).getLastRowNum();
    }

    public void renderData(Row r, Object... datas) {



        int idx = 0;

        for (Object data : datas) {

            Cell c = r.createCell(idx++);
            CellStyle style = sxssfWorkbook.createCellStyle();

            if (data  == null) {
                c.setCellValue("");
                style.setAlignment(HorizontalAlignment.LEFT);
            } else if (data instanceof String) {
                c.setCellValue((String) data);
                style.setAlignment(HorizontalAlignment.LEFT);
            } else if (data instanceof Integer) {
                c.setCellValue((int) data);
                style.setAlignment(HorizontalAlignment.RIGHT);
            } else if (data instanceof Double) {
                c.setCellValue((double) data);
                style.setAlignment(HorizontalAlignment.RIGHT);
            } else if (data instanceof BigDecimal) {
                c.setCellValue(((BigDecimal) data).doubleValue());
                style.setAlignment(HorizontalAlignment.RIGHT);
            } else if (data instanceof LocalDateTime) {
                c.setCellValue(DateUtils.format((LocalDateTime) data, "yyyy-MM-dd HH:mm:ss"));
                style.setAlignment(HorizontalAlignment.CENTER);
            } else{
                c.setCellValue(String.valueOf(data));
                style.setAlignment(HorizontalAlignment.LEFT);
            }
            c.setCellStyle(style);
        }
    }
}

