package com.example.slotweb.page.home;

import com.example.slotweb.service.pay.PayService;
import com.example.slotweb.service.slot.SlotService;
import com.example.slotweb.service.slot.dto.Slot;
import com.example.slotweb.utils.LoginUtils;
import com.example.slotweb.utils.StringUtils;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SlotService slotService;
    private final PayService payService;

    @GetMapping("/")
    public String home(Model model,
                       @RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "10") int pageSize,
                       @RequestParam(required = false) String searchText,
                       @RequestParam(required = false) String searchType
    ) {

        PageInfo<Slot> pageInfo = null;
        List<Slot> list = null;
        String memberId = LoginUtils.getMember().getMemberId();
        Integer totalSlotCnt = 0;
        List<Map<String, Object>> payTypeCntList = null;

        // searchText가 null인 경우 빈 문자열로 설정
        if (searchText == null) {
            searchText = "";
        }

        if(StringUtils.equals(LoginUtils.getMember().getRoleId(), "USER")){
            pageInfo = slotService.getSlotList(pageNum, pageSize, memberId, searchText, searchType);
            totalSlotCnt = payService.getPayCount(memberId);
            payTypeCntList = payService.getPayTypeCount(memberId);

        }else if(StringUtils.equals(LoginUtils.getMember().getRoleId(), "DS")){
            pageInfo = slotService.getDsSlotList(pageNum, pageSize, memberId, searchText, searchType);
            totalSlotCnt = payService.getDsPayCount(memberId);
            payTypeCntList = payService.getDsPayTypeCount(memberId);

        }else{
            pageInfo = slotService.getAdminSlotList(pageNum, pageSize, searchText, searchType);
            totalSlotCnt = payService.getAdminPayCount();
            payTypeCntList = payService.getAdminPayTypeCount();

        }
//        model.addAttribute("list", list);
        model.addAttribute("searchText", searchText);
        model.addAttribute("searchType", searchType);
        model.addAttribute("totalSlotCnt", totalSlotCnt);
        model.addAttribute("payTypeCntList", payTypeCntList);
        model.addAttribute("pageInfo", pageInfo);
        // model.addAttribute("usedSlotCnt", usedSlotCnt);
        //model.addAttribute("remainSlotCnt", totalSlotCnt - usedSlotCnt);

        return "home";
    }
}
