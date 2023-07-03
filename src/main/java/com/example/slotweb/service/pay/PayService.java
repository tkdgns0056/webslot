package com.example.slotweb.service.pay;

import com.example.slotweb.service.pay.dto.Pay;
import com.example.slotweb.service.slot.SlotMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PayService {

    private final PayMapper payMapper;
    private final SlotMapper slotMapper;

    public List<Pay> getPayList(String memberId) {

        return payMapper.selectPayList(memberId);
    }

    /**
     * 페이징
     * @param pageNum
     * @param pageSize
     * @param memberId
     * @return
     */
    public PageInfo<Pay> getPayList(int pageNum, int pageSize, String memberId) {
        Page<Pay> page = PageHelper.startPage(pageNum, pageSize);

        List<Pay> list = payMapper.selectPayList(memberId);

        return new PageInfo<>(list, page.getPages());
    }

    public List<Pay> getPayInfoList(String memberId) {
        return payMapper.selectPayInfoList(memberId);
    }

    @Transactional
    public void savePay(List<Pay> savePays, List<Long> deletePayNos) {

        if( deletePayNos != null && !deletePayNos.isEmpty()){
            slotMapper.deleteSlotByPayNo(deletePayNos);
            payMapper.deletePay(deletePayNos);
        }

        if( savePays != null && !savePays.isEmpty()){

            // 회원 등록/수정
            payMapper.savePay(savePays);
        }

    }


    public int getPayCount(String memberId) {
        return payMapper.selectPayCount(memberId);
    }

    public Integer getDsPayCount(String memberId) {
        return payMapper.selectDsPayCount(memberId);
    }

    public Integer getAdminPayCount() {
        return payMapper.selectAdminPayCount();
    }

    @Transactional
    public void deletePayByMemberId(List<String> deleteMemberIds) {
        payMapper.deletePayByMemberId(deleteMemberIds);
    }

    public List<Map<String, Object>> getPayTypeCount(String memberId) {

        return payMapper.selectPayTypeCount(memberId);
    }

    public List<Map<String, Object>> getDsPayTypeCount(String memberId) {
        return payMapper.selectDsPayTypeCount(memberId);
    }

    public List<Map<String, Object>> getAdminPayTypeCount() {
        return payMapper.selectAdminPayTypeCount();
    }


    /**
     * 페이징
     * @param pageNum
     * @param pageSize
     * @param memberId
     * @return
     */
    public PageInfo<Pay> getPayInfoList(int pageNum, int pageSize, String memberId) {

        Page<Pay> page = PageHelper.startPage(pageNum, pageSize);

        List<Pay> list = payMapper.selectPayInfoList(memberId);
        return new PageInfo<>(list, page.getPages());
    }

    public PageInfo<Pay> getDsMemberPayInfoList(int pageNum, int pageSize, String memberId) {
        Page<Pay> page = PageHelper.startPage(pageNum, pageSize);

        List<Pay> list = payMapper.selectDsMemberPayInfoList(memberId);

        return new PageInfo<>(list, page.getPages());
    }

    public PageInfo<Pay> getAdminMemberPayInfoList(int pageNum, int pageSize) {
        Page<Pay> page = PageHelper.startPage(pageNum, pageSize);
        List<Pay> list = payMapper.selectAdminMemberPayInfoList();
        return new PageInfo<>(list, page.getPages());
    }

    public List<Pay> getDsMemberPayInfoList( String memberId) {
        return payMapper.selectDsMemberPayInfoList(memberId);
    }

    public List<Pay> getAdminMemberPayInfoList() {

        return payMapper.selectAdminMemberPayInfoList();
    }

    @Transactional
    public void extendPay(List<Integer> extendPays, Integer extendDay) {
        payMapper.updateEndDt(extendPays, extendDay);
    }
}
