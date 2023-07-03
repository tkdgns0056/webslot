package com.example.slotweb.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * 날짜관련 클래스
 */
@Component
public class DateUtils {

    /**
     * 날짜포멧
     */
    public static String PATTERN_YEAR = "yyyy";
    public static String PATTERN_MONTH = "MM";
    public static String PATTERN_DATE = "dd";
    public static String PATTERN_YYYYMM = "yyyyMM";
    public static String PATTERN_YYYMMDD = "yyyyMMdd";
    public static String PATTERN_HMS = "HHmmss";
    public static String PATTERN_DATE_DASH = "yyyy-MM-dd";
    public static String PATTERN_DATETIME = "yyyyMMddHHmmss";

    public static String DEFAULT_TIME_ZONE_ID = "Asia/Seoul";


    /**
     * 문자열을 로컬 데이트 타임으로 반환
     */
    public static LocalDateTime of(String dateStr){
        return StrDateUtils.parseDateTime(dateStr);
    }

    /**
     * 현재 일시에 해당하는 날짜시간을 반환
     * @return
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 현재일자가 1일 인지롤 반환 한다.
     * @return
     */
    public static boolean isFirstDay() {
        return isFirstDay(now());
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
     * 주어진 일시에 해당하는 날짜시간을 주어진 포맷으로 반환한다.
     *
     * DateUtils.format(LocalDateTime.now(), "yyyyMMddHHmmss") = 20190807180301
     * DateUtils.format(LocalDateTime.now(), "yyyyMM") = 201908
     * DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss") = 2019-08-07 18:06:13
     *
     * @param dateTime 기준일시
     * @return
     */
    public static String format(LocalDateTime dateTime, String dateFormat) {
        return dateTime.format(DateTimeFormatter.ofPattern(dateFormat));
    }

     /**
     * 기준일자가 1일 인지롤 반환 한다.
     * @param dateTime 기준일자
     * @return
     */
    public static boolean isFirstDay(LocalDateTime dateTime) {
        return dateTime.get(ChronoField.DAY_OF_MONTH) == 1;
    }

     /**
     * 현재일자가 해당월의 마지막 일인지 여부를 반환 한다.
     * @return
     */
    public static boolean isLastDay() {
        return isLastDay(now());
    }

    /**
     * 기준일자가 해당월의 마지막 일인지 여부를 반환 한다.
     * @param dateTime 기준일자
     * @return
     */
    public static boolean isLastDay(LocalDateTime dateTime) {
        return dateTime.get(ChronoField.DAY_OF_MONTH) == dateTime.toLocalDate().lengthOfMonth();
    }

    /**
     * dateTime1이 dateTime2보다 크거나 같은지 여부 반환
     * @param dateTime1
     * @param dateTime2
     * @return
     */
    public static boolean greaterEquals(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isAfter(dateTime2) || dateTime1.isEqual(dateTime2);
    }


    /**
     * dateTime1이 dateTime2보다 큰지 여부 반환
     * @return
     */
    public static boolean greaterThen(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isAfter(dateTime2);
    }

    /**
     * dateTime1이 dateTime2보다 작거나 같은지 여부 반환
     * @return
     */
    public static boolean lessEquals(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return  dateTime1.isBefore(dateTime2) || dateTime1.isEqual(dateTime2);
    }

    /**
     * dateTime1이 dateTime2보다 작은지 여부 반환
     * @return
     */
    public static boolean lessThen(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isBefore(dateTime2);
    }

    /**
     * dateTime이 현재시간보다 작은지 여부 반환
     * @return
     */
    public static boolean lessThenNow(LocalDateTime dateTime) {
        return dateTime.isBefore(now());
    }

    /**
     * 현재 일시를 기준으로 주어진 날짜시간 필드에 주어진 값 만큼 더한 일시를 반환한다.
     * @return
     */
    public static LocalDateTime add(TemporalUnit field, long amountToAdd) {
        return add(now(), field, amountToAdd);
    }

    /**
     * 주어진 일시를 기준으로 주어진 날짜시간 필드에 주어진 값 만큼 더한 일시를 반환한다.
     * @return
     */
    public static LocalDateTime add(LocalDateTime dateTime, TemporalUnit field, long amountToAdd) {
        if(amountToAdd == 0) {
            return dateTime;
        }else if(amountToAdd > 0) {
            return dateTime.plus(amountToAdd, field);
        }else{
            return dateTime.minus(Math.abs(amountToAdd), field);
        }
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        // 3. Date -> LocalDateTime
        LocalDateTime localDateTime = date.toInstant() // Date -> Instant
                .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
                .toLocalDateTime(); // ZonedDateTime -> LocalDateTime

        return localDateTime;
    }


}