package com.example.slotweb.service.slot;

import com.example.slotweb.extend.ExcelSupport;
import com.example.slotweb.service.excel.ExcelMapper;
import com.example.slotweb.service.member.dto.Member;
import com.example.slotweb.service.pay.PayMapper;
import com.example.slotweb.service.pay.PayService;
import com.example.slotweb.service.pay.dto.Pay;
import com.example.slotweb.service.slot.dto.Slot;
import com.example.slotweb.utils.LoginUtils;
import com.example.slotweb.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SlotService implements ExcelSupport {

    public static final int COLUMN_KEYWORD_TYPE = 0;
    public static final int COLUMN_PRODUCT_NAME = 1;
    public static final int COLUMN_SEARCH_KEYWORD = 2;
    public static final int COLUMN_CATEGORY_NO = 4;
    public static final int COLUMN_MID = 5;
    public static final int COLUMN_MS = 6;

    private final SlotMapper slotMapper;
    private final PayService payService;
    private final ExcelMapper excelMapper;

    public PageInfo<Slot> getSlotList(int pageNum, int pageSize, String memberId, String searchText, String searchType) {

        Page<Slot> page = PageHelper.startPage(pageNum, pageSize);

        List<Slot> slotList = slotMapper.selectSlotList(memberId, searchText, searchType);

        // pageHelper utils 이용하여 리턴
        return new PageInfo<>(slotList, page.getPages());
    }

    @Transactional
    public void deleteOldSlot(List<Long> deletePayIds) {

        slotMapper.deleteSlotByPayNo(deletePayIds);
    }

    public PageInfo<Slot> getDsSlotList(int pageNum, int pageSize, String memberId, String searchText, String searchType) {
        Page<Slot> page = PageHelper.startPage(pageNum, pageSize);

        List<Slot> list = slotMapper.selectDsSlotList(memberId, searchText, searchType);

        return new PageInfo<>(list, page.getPages());
    }

    public PageInfo<Slot> getAdminSlotList(int pageNum, int pageSize, String searchText, String searchType) {
        Page<Slot> page = PageHelper.startPage(pageNum, pageSize);

        List<Slot> list = slotMapper.selectAdminSlotList(searchText, searchType);

        return new PageInfo<>(list, page.getPages());
    }

    @Transactional
    public void saveSlot(List<Slot> saveSlots, List<Long> deleteSlotNos) {

        if (deleteSlotNos != null && !deleteSlotNos.isEmpty()) {
            slotMapper.deleteSlot(deleteSlotNos);
        }

        // saveSlots가 null이 아니고 비어 있지 않으면 슬롯을 저장
        if (saveSlots != null && !saveSlots.isEmpty()) {

            Map<String, Pay> currentPay = new HashMap<>();

            Map<String, List<Pay>> payMap = payService.getPayList(LoginUtils.getMemberId())
                    .stream()
                    .collect(Collectors.groupingBy(Pay::getType));

            // 등록된 타입별 값 빼주는 작업 필요
            Map<String, Long> useSlotMap = getTypeUsedSlot(LoginUtils.getMemberId());

            // 전체 슬롯 개수와 사용된 슬롯 개수를 비교하여 결제에 사용된 슬롯을 차감하는 로직
            // 전체슬롯 - 사용슬롯
            // 2. Map.Entry를 사용하여 Map 반복

            // useSlotMap의 엔트리(키-값 쌍) 들을 반복 처리.
            // 각 엔트리는 타입(Type)과 해당 타입의 사용된 슬롯 개수
            for (Map.Entry<String, Long> entry : useSlotMap.entrySet()) {
                String key = entry.getKey();
                Long value = entry.getValue(); // 사용된 슬롯 개수

                while (true) { // 타입(Type)의 사용된 슬롯을 차감하는 작업을 처리하기 위한 반복문

                    // 현재 타입의 결제 리스트 가져옴
                    // 타입(Type)을 키(key)로 가지고 해당 타입(Type)에 대한 결제 리스트를 값(value)으로 가지는 맵
                    List<Pay> payList = payMap.get(key);

                    // 타입(Type)에 대한 결제가 더 이상 없는 경우
                    // 결제 리스트(payList)가 비어있다면 루프를 종료.
                    if (payList.isEmpty()) break;
                    // 결제 리스트(payList)에서 첫 번째 결제 정보(pay)를 가져옴
                    Pay pay = payList.get(0);

                    // 현재 결제 정보(pay)의 슬롯 개수(pay.getSlotCnt())가 사용된 슬롯 개수(value)보다 크거나같은 경우를 처리
                    // 즉, 해당 결제로 충분한 슬롯이 남은 경우
                    if (pay.getSlotCnt() >= value) {
                        // 결제 정보(pay)의 슬롯 개수에서 사용된 슬롯 개수를 차감하여 남은 슬롯 개수(remain)를 계산
                        Long remain = pay.getSlotCnt() - value;
                        // 결제 정보(pay)의 슬롯 개수를 갱신
                        pay.setSlotCnt(remain);
                        // 남은 슬롯 개수(remain)가 0인 경우,
                        // 즉 모든 슬롯을 사용한 경우에는 해당 결제 정보(pay)를 결제 리스트(payList)에음서 제거
                        if (remain == 0) {
                            payList.remove(0);
                        }
                        break;
                        // 해당 결제 정보(pay)의 슬롯은 모두 사용되었기 때문에 더 이상 사용할 수 없음
                        // 즉, 해당 결제로 충분한 슬롯이 남지 않은 경우
                    } else {
                        // 사용된 슬롯 개수(value)에서 결제 정보(pay)의 슬롯 개수를 차감하여 부족한 슬롯 개수(remain)를 계산
                        Long remain = value - pay.getSlotCnt();
                        // 결제 리스트(payList)에서 현재 결제 정보(pay)를 제거
                        payList.remove(0);
                    }
                }
            }

            saveSlots.stream().forEach(e -> {

                e.setMemberId(LoginUtils.getMemberId());

                // 새로 추가되는 슬롯인 경우를 처리
                if (e.getSlotNo() == null) {

                    Pay p = null; //결제 정보를 담을 변수 초기화

                    while (true) {
                        // 해당 슬롯의 타입(getType())에 해당하는 결제 정보 리스트(payList)를 가져옴
                        List<Pay> payList = payMap.get(e.getType());

                        if (payList == null || payList.isEmpty()) {
                            break;
                        }

                        p = payList.get(0);
                        // 해당 결제 정보의 슬롯이 모두 사용되었을 경우
                        if (p.getSlotCnt() == 0) {
                            payList.remove(0);
                            p = null;
                            continue;
                        }

                        break;
                    }

                    if (p == null) {
                        throw new RuntimeException("슬롯이 부족하여 등록 할 수 없습니다.");
                    }

                    e.setPayNo(p.getPayNo());
                    // 슬롯 등록하면 개수 차감
                    p.setSlotCnt(p.getSlotCnt() - 1);
                }
            });

            slotMapper.saveSlot(saveSlots);
        }

    }

    /**
     * 엑셀 등록
     *
     * @param uid
     * @param list     엑셀 파싱 데이터
     * @param newParam
     * @param result
     */
    public void processExcelData(String uid, List<List<String>> list, Map<String, Object> newParam, Map<String, Object> result) {

        String loginMemberId = LoginUtils.getMemberId(); // 로그인 한 유저 아이디
        Long pcSlotCount = 0L; // 결제된 PC 슬롯 개수
        Long mobileSlotCount = 0L; // 결제된 MOBILE 슬롯 개수
        Long pcUsedSlotCount = 0L; // 사용중인 pc 슬롯 개수
        Long mobileUsedSlotCount = 0L; // 사용중인 mobile 슬롯 개수
        Long availablePcSlots = 0L; // 사용 가능한 pc 슬롯 개수
        Long availableMobileSlots = 0L; // 사용 가능한 mobile 슬롯 개수


        // 유저의 슬롯 결제 정보
        Map<String, List<Pay>> payMap = payService.getPayList(loginMemberId)
                .stream()
                .collect(Collectors.groupingBy(Pay::getType));

        // 유저가 결제한 PC 슬롯 개수
        pcSlotCount = payMap.getOrDefault("P", payMap.getOrDefault("p", Collections.emptyList()))
                .stream()
                .mapToLong(Pay::getSlotCnt)
                .sum();

        // 유저가 결제한 Mobile 슬롯 개수
        mobileSlotCount = payMap.getOrDefault("M", payMap.getOrDefault("m", Collections.emptyList()))
                .stream()
                .mapToLong(Pay::getSlotCnt)
                .sum();


        // payService를 통해 결제 정보 가져오기
        List<Pay> payList = payService.getPayList(loginMemberId);

        // 사용 중인 슬롯 타입별 개수를 저장할 맵
        Map<String, Long> usedSlotTypeCountMap = new HashMap<>();
        // 사용 중인 슬롯 정보 가져오기
        List<Map<String, Long>> usedSlotCounts = slotMapper.selectTypeUsedSlot(loginMemberId);

        // 사용 중인 슬롯 정보 맵으로 변환(타입별로 뽑아내기위해)
        for (Map<String, Long> slotCount : usedSlotCounts) {
            String slotType = String.valueOf(slotCount.get("type"));
            Long count = Long.parseLong(String.valueOf(slotCount.get("count")));
            usedSlotTypeCountMap.put(slotType, count);
        }
        // 각 타입별 사용 중인 슬롯 개수 확인
        pcUsedSlotCount = usedSlotTypeCountMap.getOrDefault("P", usedSlotTypeCountMap.getOrDefault("p", 0L)); // 이미 사용중인 pc 슬롯 개수
        mobileUsedSlotCount = usedSlotTypeCountMap.getOrDefault("M", usedSlotTypeCountMap.getOrDefault("m", 0L)); // 이미 사용중인 mobile 슬롯 개수

        availablePcSlots = pcSlotCount - pcUsedSlotCount; // 사용가능한 pc 슬롯
        availableMobileSlots = mobileSlotCount - mobileUsedSlotCount; // Mobile 슬롯의 사용 가능한 개수

        List<Slot> slots = new ArrayList<>();
        try {
            // 가져온 결제 정보를 사용하여 필요한 로직 수행
            if (!payList.isEmpty()) {
                // 가져온 payNo와 endDt를 활용하여 필요한 처리 수행
                // 데이터 매핑 및 저장 (키워드 유형 부터)
                for (int i = 0; i < list.size(); i++) {
                    List<String> row = list.get(i);

                    if (StringUtils.isEmpty(row.get(0))) {
                        throw new RuntimeException("[ERROR][" + (i + 2) + "행] 키워드 유형을 입력하여 주시기 바랍니다.");
                    }
                    if (StringUtils.isEmpty(row.get(1))) {
                        throw new RuntimeException("[ERROR][" + (i + 2) + "행] 상품명을 입력하여 주시기 바랍니다.");
                    }
                    if (StringUtils.isEmpty(row.get(2))) {
                        throw new RuntimeException("[ERROR][" + (i + 2) + "행] 검색키워드를 입력하여 주시기 바랍니다.");
                    }
                    if (StringUtils.isEmpty(row.get(4))){
                        throw new RuntimeException("[ERROR][" + (i + 2) + "행] 카테고리 번호을 입력하여 주시기 바랍니다.");
                    }
                    if (!StringUtils.isNumeric(row.get(4))){
                        throw new RuntimeException("[ERROR][" + (i + 2) + "행] 카테고리 번호는 숫자만 입력 가능합니다.");
                    }
                    if(StringUtils.isEmpty(row.get(5))){
                        throw new RuntimeException("[ERROR][" + (i + 2) + "행] MID값을 입력하여 주시기 바랍니다.");
                    }
                    if (!StringUtils.isNumeric(row.get(5))){
                        throw new RuntimeException("[ERROR][" + (i + 2) + "행] MID값은 숫자만 입력 가능합니다.");
                    }
                    if(StringUtils.isEmpty(row.get(6))) {
                        throw new RuntimeException("[ERROR][" + (i + 2) + "행] MS값을 입력하여 주시기 바랍니다.");
                    }
                    if (!StringUtils.isNumeric(row.get(6))){
                        throw new RuntimeException("[ERROR][" + (i + 2) + "행] MS값은 숫자만 입력 가능합니다.");
                    }


                    String keywordType = row.get(0); // "키워드 유형" 컬럼 데이터

                    // 슬롯 저장 시 타입 별 개수 비교하는 유효성 검사(PC, MOBILE 부분 검사)
                    if ("P".equalsIgnoreCase(keywordType)) {
                        if (availablePcSlots <= 0) {
                            int pcDataCount = countTypeData(list, "P");
                            throw new RuntimeException(MessageFormat.format("PC 슬롯이 부족합니다. \n결제한 PC 슬롯 개수: {0} \n사용중인 PC 슬롯 개수:  {1} \n엑셀에 등록된 PC 타입 데이터 개수: {2}", pcSlotCount, pcUsedSlotCount, pcDataCount));
                        } else {
                            Pay pay = getMatchingPay(payMap, "P");
                            setSlotWithPayInfo(loginMemberId, row, slots, pay);
                            availablePcSlots--; // 사용한 PC 슬롯 개수를 감소시킴

                        }
                    } else if ("M".equalsIgnoreCase(keywordType)) {
                        if (availableMobileSlots <= 0) {
                            int mobileDataCount = countTypeData(list, "M");
                            throw new RuntimeException(MessageFormat.format("Mobile 슬롯이 부족합니다. \n결제한 MOBILE 슬롯 개수: {0} \n사용중인 MOBILE 슬롯 개수:  {1} \n엑셀에 등록된 MOBILE 타입 데이터 개수: {2}", mobileSlotCount, mobileUsedSlotCount, mobileDataCount));
                        } else {
                            Pay pay = getMatchingPay(payMap, "M");

                            setSlotWithPayInfo(loginMemberId, row, slots, pay);
                            availableMobileSlots--; // 사용한 Mobile 슬롯 개수를 감소시킴
                        }

                    } else {
                        throw new RuntimeException("[ERROR][" + (i + 2) + "행] M 이나 P 키워드유형으로 입력해주세요.");
                    }
                }

            } else {
                // 결제 정보가 없는 경우에 대한 처리
                throw new RuntimeException("결제된 슬롯이 없습니다.");
                //result.put("error", "결제된 슬롯이 없습니다.");
            }

            // 데이터 매핑 후 DB에 저장하는 로직 수행
            excelMapper.saveExcelSlot(slots);
            result.put("success", "업로드 성공");

        } catch (RuntimeException e) {
            result.put("error", e.getMessage());

        } catch (Exception e) {
            // 예외 처리
            result.put("error", "[ERROR]  슬롯 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }


    // 각 타입별로 결제된 슬롯에 맞는 payNo 매핑
    private Pay getMatchingPay(Map<String, List<Pay>> payMap, String type) {
        List<Pay> payList = payMap.getOrDefault(type, Collections.emptyList());
        for (Pay pay : payList) {
            if (pay.getSlotCnt() > 0) {
                pay.setSlotCnt(pay.getSlotCnt() - 1); // 결제한 슬롯 개수를 하나 감소시킴
                return pay;
            }
        }
        return null; // 모든 Pay 객체를 순회했으나 반환할 Pay 객체가 없는 경우 null 반환
    }

    // 숫자 유효성 검사 메서드
    private boolean isValidNumeric(String value) {
        return value.matches("\\d+");
    }

    // 타입별 데이터 개수를 세는 메서드
    private int countTypeData(List<List<String>> list, String type) {
        int count = 0;
        String lowercaseType = type.toLowerCase(); // 입력된 타입을 소문자로 변경

        for (List<String> row : list) {
            String keywordType = row.get(0); // 리스트에서 가져온 타입
            if (lowercaseType.equalsIgnoreCase(keywordType)) { // 대소문자 구분 없이 비교
                count++;
            }
        }

        return count;
    }
    // 엑셀 순회하는 데이터
    private void setSlotWithPayInfo(String memberId, List<String> row, List<Slot> slots, Pay pay) {
        // 기존 코드 내용

        // 위의 로직 추가
        String keywordType = row.get(0); // "키워드 유형" 컬럼의 데이터
        String productName = row.get(1); // "상품명" 컬럼의 데이터
        String keyword = row.get(2); // "검색키워드" 컬럼의 데이터
        String subKeyword = row.get(3); // "서브키워드" 컬럼의 데이터
        Long categoryNo = Long.parseLong(row.get(4)); // "카테고리번호" 컬럼의 데이터
        Long mId = Long.parseLong(row.get(5)); // "MID값" 컬럼의 데이터
        Long ms = Long.parseLong(row.get(6)); // "MS값" 컬럼의 데이터
        String memo = row.get(7); // "메모" 컬럼의 데이터

        Slot slot = new Slot();
        slot.setType(keywordType);
        slot.setProductName(productName);
        slot.setKeyword(keyword);
        slot.setSubKeyword(subKeyword);
        slot.setCategoryNo(categoryNo);
        slot.setMid(mId);
        slot.setMs(ms);
        slot.setMemo(memo);

        slot.setMemberId(memberId);
        slot.setPayNo(pay.getPayNo());
        slot.setExpireDt(pay.getEndDt());

        slots.add(slot);
    }


    public Integer getSlotCount(String memberId) {
        return slotMapper.selectSlotCount(memberId);
    }

    public Integer getDsSlotCount(String memberId) {
        return slotMapper.selectDsSlotCount(memberId);
    }

    public Integer getAdminSlotCount() {
        return slotMapper.selectAdminSlotCount();
    }

    @Transactional
    public void deleteSlotByMemberId(List<String> deleteMemberIds) {

        slotMapper.deleteSlotByMemberId(deleteMemberIds);
    }


    // 결제한 유저의 슬롯 타입
    public Map<String, Long> getTypeUsedSlot(String memberId) {
        // 현재 등록한 유저의 슬롯 개수를 가져오는 부분. ex) 모바일 슬롯 3개, PC슬롯 1개 구입 했는데, 등록한 슬롯은 현재 모바일 슬롯 1개 즉, 1/3 형태
        return slotMapper.selectTypeUsedSlot(LoginUtils.getMemberId())
                .stream()
                .map( e -> {
                    Map<String, Long> map = new HashMap<>();
                    map.put(String.valueOf(e.get("type")), Long.valueOf(e.get("count")));
                    return map;
                })
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.summingLong(Map.Entry::getValue)
                ));
    }

//    public Map<String, Integer> getTypeUsedSlot(String memberId) {
//        List<Map<String, Integer>> result = slotMapper.selectTypeUsedSlot(LoginUtils.getMemberId());
//        Map<String, Integer> typeUsedSlotMap = new HashMap<>();
//
//        for (Map<String, Integer> entry : result) {
//            for (Map.Entry<String, Integer> typeEntry : entry.entrySet()) {
//                String type = typeEntry.getKey();
//                Integer count = typeEntry.getValue();
//
//                typeUsedSlotMap.put(type, typeUsedSlotMap.getOrDefault(type, 0) + count);
//            }
//        }
//
//        return typeUsedSlotMap;
//    }

    @Transactional
    public void deleteBatchSlot() {

        slotMapper.deleteBatchSlot();
    }

    @Override
    public int getLastCell(String uid) {
        return 9;
    }
}
