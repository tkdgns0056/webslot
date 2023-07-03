package com.example.slotweb.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalUnit;

/**
 * 날짜관련 클래스
 */
public class StrDateUtils {

	
	private StrDateUtils() {
		 throw new AssertionError();
	}
	
    /**
     * 날짜포멧
     */
    public static String PATTERN_YEAR = "yyyy";
    public static String PATTERN_MONTH = "yyyyMM";
    public static String PATTERN_DATE = "yyyyMMdd";
    public static String PATTERN_HMS = "HHmmss";
    public static String PATTERN_DATE_DASH = "yyyy-MM-dd";
    public static String PATTERN_DATETIME = "yyyyMMddHHmmss";


    /**
     * 현재 일시에 해당하는 날짜시간을 yyyyMMddHHmmss 포맷으로 반환한다.
     * DateUtils.now() = 20190807180301
     * @return
     */
    public static String now() {
        return now(PATTERN_DATETIME);
    }

    /**
     * 현재 일시에 해당하는 날짜시간을 주어진 포맷으로 반환한다.
     *
     * DateUtils.format("yyyy-MM-dd") = 2019-08-07
     * DateUtils.format("yyyyMM") = 201908
     * DateUtils.format("yyyy-MM-dd HH:mm:ss") = 2019-08-07 18:06:13
     *
     * @param dateFormat 날짜 포맷
     * @return
     */
    public static String now(String dateFormat) {
        return format(LocalDateTime.now(), dateFormat);
    }

    /**
     * 현재일자가 1일 인지롤 반환 한다.
     *
     * 만약 오늘 날짜가 2021-06-24 라면
     * assertFalse(JDateUtils.isFirstDay());
     *
     * 만약 오늘 날짜가 2021-06-01 라면
     * assertTrue(JDateUtils.isFirstDay());
     *
     * @return
     */
    public static boolean isFirstDay() {
        return isFirstDay(now(PATTERN_DATE));
    }

     /**
     * 기준일자가 1일 인지롤 반환 한다.
     *
     * assertTrue(JDateUtils.isFirstDay("20210601"));
     * assertTrue(JDateUtils.isFirstDay("2021060100"));
     * assertTrue(JDateUtils.isFirstDay("202106010000"));
     * assertTrue(JDateUtils.isFirstDay("20210601000000"));
     * assertTrue(JDateUtils.isFirstDay("20210601 00:00:00"));
     * assertTrue(JDateUtils.isFirstDay("2021-06-01 00:00:00"));
     * assertFalse(JDateUtils.isFirstDay("01"));
     * assertFalse(JDateUtils.isFirstDay("2021"));
     * assertFalse(JDateUtils.isFirstDay("202106"));
     *
     * @param dateStr 기준일자
     * @return
     */
    public static boolean isFirstDay(String dateStr) {
        LocalDate date = parseDate(dateStr);
        if( date == null){
            return false;
        }
        return date.get(ChronoField.DAY_OF_MONTH) == 1;
    }

     /**
     * 현재일자가 해당월의 마지막 일인지 여부를 반환 한다.
     *
     * 만약 오늘 날짜가 2021-06-24 라면
     * assertFalse(JDateUtils.isLastDay());
     *
     * 만약 오늘 날짜가 2021-06-30 라면
     * assertTrue(JDateUtils.isLastDay());
     *
     * @return
     */
    public static boolean isLastDay() {
        return isLastDay(now(PATTERN_DATE));
    }

    /**
     * 기준일자가 해당월의 마지막 일인지 여부를 반환 한다.
     *
     * assertFalse(JDateUtils.isLastDay(null));
     * assertFalse(JDateUtils.isLastDay("20210624"));
     * assertTrue(JDateUtils.isLastDay("20210630"));
     * assertTrue(JDateUtils.isLastDay("20210630000000"));
     * assertTrue(JDateUtils.isLastDay("20210630 00:00:00"));
     * assertTrue(JDateUtils.isLastDay("2021-06-30 00:00:00"));
     * assertTrue(JDateUtils.isLastDay("2021-06-30"));
     * assertFalse(JDateUtils.isLastDay("2021-06-29"));
     *
     * @param dateStr 기준일자
     * @return
     */
    public static boolean isLastDay(String dateStr) {
        LocalDate date = parseDate(dateStr);
        if( date == null) {
            return false;
        }
        return date.get(ChronoField.DAY_OF_MONTH) == date.lengthOfMonth();
    }

    /**
     * dateStr1이 dateStr2보다 크거나 같은지 여부 반환
     * assertTrue(JDateUtils.greaterEquals("2022-03-26", "2022-03-26"));
     * assertTrue(JDateUtils.greaterEquals("2022-03-27", "2022-03-26"));
     * assertFalse(JDateUtils.greaterEquals("2022-03-23", "2022-03-26"));
     * @param dateStr1
     * @param dateStr2
     * @return
     */
    public static boolean greaterEquals(String dateStr1, String dateStr2) {
        if( parseDateTime(dateStr1).isAfter(parseDateTime(dateStr2))
                || parseDateTime(dateStr1).isEqual(parseDateTime(dateStr2))){
            return true;
        }
        return false;
    }

    /**
     * dateStr1이 dateStr2보다 큰지 여부 반환
     * assertTrue(JDateUtils.greaterThen("2022-03-28", "2022-03-26"));
     * assertTrue(JDateUtils.greaterThen("2022-03-27", "2022-03-26"));
     * assertFalse(JDateUtils.greaterThen("2022-03-23", "2022-03-26"));
     * @return
     */
    public static boolean greaterThen(String dateStr1, String dateStr2) {
        if( parseDateTime(dateStr1).isAfter(parseDateTime(dateStr2))){
            return true;
        }
        return false;
    }

    /**
     * dateStr1이 dateStr2보다 작거나 같은지 여부 반환
     * assertTrue(JDateUtils.lessEquals("2022-03-21", "2022-03-26"));
     * assertTrue(JDateUtils.lessEquals("2022-03-26", "2022-03-26"));
     * assertFalse(JDateUtils.lessEquals("2022-03-30", "2022-03-26"));
     * @return
     */
    public static boolean lessEquals(String dateStr1, String dateStr2) {
        if( parseDateTime(dateStr1).isBefore(parseDateTime(dateStr2))
                || parseDateTime(dateStr1).isEqual(parseDateTime(dateStr2))){
            return true;
        }
        return false;
    }

    /**
     * dateStr1이 dateStr2보다 작은지 여부 반환
     * assertFalse(JDateUtils.lessThen("2022-03-26", "2022-03-26"));
     * assertTrue(JDateUtils.lessThen("2022-03-23", "2022-03-26"));
     * assertFalse(JDateUtils.lessThen("2022-03-30", "2022-03-26"));
     * @return
     */
    public static boolean lessThen(String dateStr1, String dateStr2) {
        if( parseDateTime(dateStr1).isBefore(parseDateTime(dateStr2))){
            return true;
        }
        return false;
    }

    /**
     * dateStr이 현재시간보다 작은지 여부 반환
     * 현재일자가 '2022-03-23' 인 경우
     * assertFalse(JDateUtils.lessThenNow("2022-03-26"));
     * assertTrue(JDateUtils.lessThenNow("2022-03-22"));
     * @return
     */
    public static boolean lessThenNow(String dateStr) {
        if( parseDateTime(dateStr).isBefore(LocalDateTime.now())){
            return true;
        }
        return false;
    }

    /**
     * 현재 일시를 기준으로 주어진 날짜시간 필드에 주어진 값 만큼 더한 일시를 반환한다.
     * 현재일자가 '2022-03-23' 인 경우
     * assertEquals("20220324155409", JDateUtils.add(ChronoUnit.DAYS, 1, JDateUtils.PATTERN_DATE_DASH));
     * assertEquals("20220322155409", JDateUtils.add(ChronoUnit.DAYS, -1, JDateUtils.PATTERN_DATE_DASH));
     * @return
     */
    public static String add(TemporalUnit field, long amountToAdd) {
        return format(add(LocalDateTime.now(), field, amountToAdd), PATTERN_DATETIME);
    }

    /**
     * 현재 일시를 기준으로 주어진 날짜시간 필드에 주어진 값 만큼 더한 일시를 반환한다.
     * 현재일자가 '2022-03-23' 인 경우
     * assertEquals("2022-03-24", JDateUtils.add(ChronoUnit.DAYS, 1, JDateUtils.PATTERN_DATE_DASH));
     * assertEquals("2022-03-22", JDateUtils.add(ChronoUnit.DAYS, -1, JDateUtils.PATTERN_DATE_DASH));
     * @param
     * @return
     */
    public static String add(TemporalUnit field, long amountToAdd, String dateFormat) {
        return format(add(LocalDateTime.now(), field, amountToAdd), dateFormat);
    }

    /**
     * 주어진 일시를 기준으로 주어진 날짜시간 필드에 주어진 값 만큼 더한 일시를 반환한다.
     * assertEquals("2022-03-26", JDateUtils.add("2022-03-25" ,ChronoUnit.DAYS, 1, JDateUtils.PATTERN_DATE_DASH));
     * assertEquals("2022-03-24", JDateUtils.add("2022-03-25", ChronoUnit.DAYS, -1, JDateUtils.PATTERN_DATE_DASH));
     * assertEquals("2022-03-24", JDateUtils.add("20220325000000", ChronoUnit.DAYS, -1, JDateUtils.PATTERN_DATE_DASH));
     * assertEquals("2022-03-24", JDateUtils.add("2022-03-25 00:00:00", ChronoUnit.DAYS, -1, JDateUtils.PATTERN_DATE_DASH));
     * @param dateStr
     * @return
     */
    public static String add(String dateStr, TemporalUnit field, long amountToAdd, String dateFormat) {
        return format(add(parseDateTime(dateStr), field, amountToAdd), dateFormat);
    }

    /**
     * 주어진 일시에 해당하는 날짜시간을 주어진 포맷으로 반환한다.
     *
     * DateUtils.format(LocalDateTime.now(), "yyyyMMddHHmmss") = 20190807180301
     * DateUtils.format(LocalDateTime.now(), "yyyyMM") = 201908
     * DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss") = 2019-08-07 18:06:13
     *
     * @param dateTime 기준일시
     * @return
     */
    private static String format(LocalDateTime dateTime, String dateFormat) {
        return dateTime.format(DateTimeFormatter.ofPattern(dateFormat));
    }

    public static LocalDateTime add(LocalDateTime dateTime, TemporalUnit field, long amountToAdd) {
        if(amountToAdd == 0) {
            return dateTime;
        }else if(amountToAdd > 0) {
            return dateTime.plus(amountToAdd, field);
        }else{
            return dateTime.minus(Math.abs(amountToAdd), field);
        }
    }

    /**
     * 문자열을 로컬데이트 타임으로 변환
     * DateUtils.toLocalDateTime('20190807180301') = LocalDateTime Obj
     * @param dateStr 문자열 날짜
     * @return
     */
    public static LocalDateTime toLocalDateTime(String dateStr) {
        return parseDateTime(dateStr);
    }

    public static LocalDate parseDate(String dateStr) {
        dateStr = StringUtils.replace(dateStr, "", " ", "-", ":");
        if( dateStr == null || dateStr.length() < 8){
            throw new IllegalArgumentException("Invalid date format!");
        }
        return parse(dateStr.substring(0, 8), PATTERN_DATE);
    }

    public static LocalDateTime parseDateTime(String dateStr) {
        dateStr = StringUtils.replace(dateStr, "", " ", "-", ":");
        if( dateStr == null || dateStr.length() < 8){
            throw new IllegalArgumentException("Invalid date format!");
        }
        return parse(StringUtils.concat(dateStr, StringUtils.repeat("0", 14 - dateStr.length())), PATTERN_DATETIME);
    }

    public static <T> T parse(String dateStr, String dateFormat) {
        if( StringUtils.equalsLength(dateStr, 8)){
            return (T)LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(dateFormat));
        }else if( StringUtils.equalsLength(dateStr, 14)){
            return (T)LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(dateFormat));
        }
        return null;
    }
}