package com.example.slotweb.view;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

public class ExcelListView extends AbstractView {

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {

        OutputStream os = null;
        InputStream is = null;
        try {
            ClassPathResource template = (ClassPathResource)model.get("template");
            String saveFileName = (String)model.get("saveName");
            Workbook workbook = (Workbook)model.get("workbook");

            is = template.getInputStream();

            String userAgent = request.getHeader("User-Agent");
            // < MSIE 10
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                saveFileName = URLEncoder.encode(saveFileName, "UTF-8").replaceAll("\\+", "\\ ");
            }
            // the others
            else {
                saveFileName = URLEncoder.encode(saveFileName, "UTF-8").replaceAll("\\+", "%20");
            }

            response.setHeader("Content-Type", "application/octet-stream");

            if(userAgent.contains("Firefox")) {
                response.setHeader("Content-Disposition", "attachment; filename=" + saveFileName + ".xlsx; filename*=UTF-8''" + saveFileName + ".xlsx;");
            } else {
                response.setHeader("Content-Disposition", "attachment; filename=" + saveFileName + ".xlsx");
            }
            response.setHeader("Set-Cookie", "fileDownload=true; path=/");//중요

            os = response.getOutputStream();
            workbook.write(os);
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
