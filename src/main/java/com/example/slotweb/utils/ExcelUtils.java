package com.example.slotweb.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 엑셀 파일을 읽어 반환해주는 파서
 *
 * @author https://sunghs.tistory.com
 * @see <a href="https://github.com/sunghs/java-utils">source</a>
 */
public class ExcelUtils {

    private static final DataFormatter formatter = new DataFormatter();


    public static List<List<String>> parse(final MultipartFile multipartFil, final int sheetIndex, final int startIndex, final int lastCell)
        throws IOException {
        Workbook workbook = new XSSFWorkbook(multipartFil.getInputStream());
        Sheet sheet = workbook.getSheetAt(sheetIndex);

        try{

            int rowCount = sheet.getPhysicalNumberOfRows();

            if (rowCount <= 1) {
                return null;
            }

            List<List<String>> result = new ArrayList<>();

            for (int i = startIndex; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                List<String> items = new ArrayList<String>();
                for (int j = 0; j < lastCell; j++) {
                    Cell cursor = row.getCell(j);
                    String value = formatter.formatCellValue(cursor);
                    items.add(StringUtils.isNotEmpty(value) ? value : "");
                }
                result.add(items);
            }
            return result;
        }finally {
            workbook.close();
        }

    }

}
