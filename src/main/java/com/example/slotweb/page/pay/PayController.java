package com.example.slotweb.page.pay;

import com.example.slotweb.service.pay.PayService;
import com.example.slotweb.service.pay.dto.Pay;
import com.example.slotweb.utils.LoginUtils;
import com.example.slotweb.utils.StringUtils;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    @GetMapping("/pay/list")
    public String listMember(Model model,
                             @RequestParam(defaultValue = "1") int pageNum,
                             @RequestParam(defaultValue = "10") int pageSize,
                             @RequestParam String memberId) {



        PageInfo<Pay> pageInfo = payService.getPayList(pageNum, pageSize, memberId);

        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("memberId", memberId);
        return "pay/list";
    }

    /**
     * 결제한 회원 정보 조회 시
     * @param model
     * @param pageNum
     * @param pageSize
     * @param memberId
     * @return
     */
    @GetMapping("/pay/info/member/list")
    public String listPayMemberInfo(Model model,
                                    @RequestParam(defaultValue = "1") int pageNum,
                                    @RequestParam(defaultValue = "10") int pageSize,
                                    @RequestParam String memberId) {

        PageInfo<Pay> pageInfo = null;

        if (StringUtils.equals(LoginUtils.getMember().getRoleId(), "USER")) {
            pageInfo = payService.getPayInfoList(pageNum, pageSize, LoginUtils.getMemberId());

        } else if (StringUtils.equals(LoginUtils.getMember().getRoleId(), "DS")) {
            pageInfo = payService.getPayInfoList(pageNum, pageSize, memberId);

        } else {
            pageInfo = payService.getPayInfoList(pageNum, pageSize, memberId);
        }

        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("memberId", memberId);

        return "pay/info-pay-list";
    }

    @GetMapping("/pay/info/list")
    public String listPayInfo(Model model,
                              @RequestParam(defaultValue = "1") int pageNum,
                              @RequestParam(defaultValue = "10") int pageSize
    ) {

        List<Pay> list = null;
        PageInfo<Pay> pageInfo = null;

        if(StringUtils.equals(LoginUtils.getMember().getRoleId(), "USER")){
            pageInfo = payService.getPayInfoList(pageNum, pageSize, LoginUtils.getMemberId());

        }else if(StringUtils.equals(LoginUtils.getMember().getRoleId(), "DS")){
            pageInfo = payService.getDsMemberPayInfoList(pageNum, pageSize, LoginUtils.getMemberId());
            model.addAttribute("pageInfo", pageInfo);

        }else{
            pageInfo = payService.getAdminMemberPayInfoList(pageNum, pageSize);
            model.addAttribute("pageInfo", pageInfo);
        }

        model.addAttribute("pageInfo", pageInfo);

        return "pay/info-list";
    }

    @PostMapping("/pay/save")
    @ResponseBody
    public ResponseEntity<?> saveMember(@RequestBody Pay.Save savePay) {

        try {
            payService.savePay(savePay.getSavePays(), savePay.getDeletePayIds());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/pay/extend")
    @ResponseBody
    public ResponseEntity<?> extendPay(@RequestBody Pay.Extend extendPay) {

        try {
            List<Integer> distinctPayNos = extendPay.getExtendPayNos().stream()
                    .distinct()
                    .collect(Collectors.toList());


            payService.extendPay(distinctPayNos, extendPay.getExtendDay());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
