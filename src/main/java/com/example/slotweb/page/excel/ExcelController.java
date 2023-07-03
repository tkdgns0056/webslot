package com.example.slotweb.page.excel;


import com.example.slotweb.extend.ExcelContext;
import com.example.slotweb.extend.ExcelSupport;
import com.example.slotweb.service.pay.dto.Pay;
import com.example.slotweb.service.slot.SlotService;
import com.example.slotweb.utils.*;
import com.example.slotweb.view.ExcelFormView;
import com.example.slotweb.view.ExcelListView;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 엑셀 컨트롤러
 */
@Controller
@RequiredArgsConstructor
public class ExcelController {

    private final SlotService slotService;

    /**
     * 대용량 메모리 -> 디스크 플러시 사이즈
     */
    private static final int DISK_FLUSH_SIZE = 50000;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/excel/list")
    public String listMember(Model model) {
        LoginUtils.getMember();
        return "export/list";
    }

    /**
     * 엑셀 다운로드
     */
    @RequestMapping("/excel/download")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public Object download(@RequestParam(name = "serviceName", required = false) String serviceName
            , @RequestParam(name = "templateName", required = false) String templateName
            , @RequestParam(name = "saveName", required = false) String saveName
            , @RequestParam(name = "uid", required = false) String uid
            , @RequestParam Map<String, Object> param
            , final Model model
            , HttpServletRequest request) throws Exception {

        WebUtils.setAttribute(WebUtils.OPT_RESPONSE_JSON, true);

        if (StringUtils.isEmpty(serviceName)) {
            throw new RuntimeException("서비스명을 찾을 수 없습니다.");
        }

        if (StringUtils.isEmpty(templateName)) {
            throw new RuntimeException("템플릿명을 찾을 수 없습니다.");
        }

        Map newParam = new HashMap<>();

        for (String key : param.keySet()) {
            if (StringUtils.matches(key, "^\\[__\\w+__\\]$")) {
                newParam.put(StringUtils.replace(key, "", "[__", "__]"), param.get(key));
            }
        }

        //TODO [정황진] 프로퍼티로로 템플릿 루트 빼기
        ClassPathResource template = new ClassPathResource(MessageFormat.format("{0}/{1}.xlsx", "templates/excel", templateName));

        if( !template.exists()){
            throw new RuntimeException("템플릿 리소스를 찾을 수 없습니다");
        }

        ZipSecureFile.setMinInflateRatio(0);

        // 엑셀템플릿파일 지정 (지정안하고 빈 통합문서로도 가능)
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(template.getInputStream());
        // SXSSF 생성
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(xssfWorkbook, DISK_FLUSH_SIZE);

        ExcelContext excelContext = new ExcelContext();
        excelContext.setXssfWorkbook(xssfWorkbook);
        excelContext.setSxssfWorkbook(sxssfWorkbook);

        try {
            ExcelSupport excelSopport = BeanUtils.getBean(serviceName);
            excelSopport.buildExcelData(uid, newParam, excelContext);
        }catch (Exception e){
            e.printStackTrace();
            sxssfWorkbook.close();
            xssfWorkbook.close();

            Map<String, Object> result = new HashMap<>();
            result.put("message", e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        model.addAttribute("template", template);
        model.addAttribute("saveName", saveName);
        model.addAttribute("workbook", sxssfWorkbook);

        return new ExcelListView();
    }

    /**
     * 엑셀 업로드
     */
    @RequestMapping("/excel/upload")
    @ResponseBody
    public Object upload(
              @RequestParam(name = "uploadFile", required = false) MultipartFile uploadFile
            , @RequestParam(name = "serviceName", required = false) String serviceName
            , @RequestParam(name = "uid", required = false) String uid
            , @RequestParam Map<String, Object> param) throws Exception {

        if (uploadFile.isEmpty()) {
            throw new RuntimeException("엑셀파일을 찾을 수 없습니다");
        }

        if (StringUtils.isEmpty(serviceName)) {
            throw new RuntimeException("서비스명을 찾을 수 없습니다");
        }

//        if (StringUtils.isEmpty(uid)) {
//            throw new BizException(BizErrorCode.ERROR_PARAMETER_NOT_FOUND, "고유아이디");
//        }

        Map<String, Object> newParam = new HashMap<>();

        for (String key : param.keySet()) {
            if (StringUtils.matches(key, "^\\[__\\w+__\\]$")) {
                newParam.put(StringUtils.replace(key, "", "[__", "__]"), param.get(key));
            }
        }

        Map<String, Object> result = new HashMap<>();

        try {
            ExcelSupport excelSupport = BeanUtils.getBean(serviceName);
            List<List<String>> list = ExcelUtils.parse(uploadFile, 0, excelSupport.getSheetCount(), excelSupport.getLastCell(uid));
            slotService.processExcelData(uid, list, newParam, result);
        } catch (NoSuchBeanDefinitionException e) {
            throw new RuntimeException("템플릿 리소스를 찾을 수 없습니다");
        }
        return result;
    }
}
