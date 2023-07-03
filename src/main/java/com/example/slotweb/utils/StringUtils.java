package com.example.slotweb.utils;


import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
import java.util.regex.Pattern;

public class StringUtils {

	private StringUtils() {
		throw new AssertionError();
	}

	public static final char DEFAULT_REPLACE = '*';
	public static final String DEFAULT_CHAR_SET = "UTF-8";

	/**
	 * 문자열이 비교대상 문자열보다 큰지 여부를 반환한다.
	 *
	 *  StringUtils.greater("12", "10")   == true
	 *  StringUtils.greater("10", "12")   == false
	 *  StringUtils.greater("10", "10")   == false
	 *
	 * @param org 문자열
	 * @param comp 비교대상 문자열
	 * @return 문자열이 비교대상 문자열보다 크다면 true를 반환하고 그렇지 않다면 false를 반환한다.
	 */
	public static boolean greater(String org, String comp) {
		return compare(org, comp) > 0 ? true : false;
	}

	/**
	 * 문자열이 비교대상 문자열보다 크거나 같은지 여부를 반환한다.
	 *
	 * StringUtils.greaterThen("12", "10")   == true
	 * StringUtils.greaterThen("10", "12")   == false
	 * StringUtils.greaterThen("10", "10")   == true
	 *
	 * @param org 문자열
	 * @param comp 비교대상 문자열
	 * @return 문자열이 비교대상 문자열보다 크거나 같다면 true를 반환하고 그렇지 않다면 false를 반환한다.
	 */
	public static boolean greaterThen(String org, String comp) {
		return compare(org, comp) >= 0 ? true : false;
	}

	/**
	 * 문자열이 비교대상 문자열보다 작은지 여부를 반환한다.
	 *
	 * StringUtils.less("12", "10")   == false
	 * StringUtils.less("10", "12")   == true
	 * StringUtils.less("10", "10")   == false
	 *
	 * @param org 문자열
	 * @param comp 비교대상 문자열
	 * @return 문자열이 비교대상 문자열보다 작다면 true를 반환하고 그렇지 않다면 false를 반환한다.
	 */
	public static boolean less(String org, String comp) {
		return compare(org, comp) < 0 ? true : false;
	}

	/**
	 * 문자열이 비교대상 문자열보다 작거나 같은지 여부를 반환한다.
	 *
	 * StringUtils.lessThen("12", "10")   == false
	 * StringUtils.lessThen("10", "12")   == true
	 * StringUtils.lessThen("10", "10")   == true
	 *
	 * @param org 문자열
	 * @param comp 비교대상 문자열
	 * @return 문자열이 비교대상 문자열보다 작거나 같다면 true를 반환하고 그렇지 않다면 false를 반환한다.
	 */
	public static boolean lessThen(String org, String comp) {
		return compare(org, comp) <= 0 ? true : false;
	}

	/**
	 * 문자열이 비교대상문자열과 같은지 여부 반환
	 *
	 * StringUtils.equals(null, null)   == true
	 * StringUtils.equals(null, "a")   == false
	 * StringUtils.equals("b", null)   == false
	 * StringUtils.equals("b", "b")   == true
	 *
	 * @param org 문자
	 * @param comp 비교대상 문자열
	 * @return
	 */
	public static boolean equals(String org, String comp) {
		if (org == comp) {
			return true;
		}
		if (org == null) {
			return comp.equals(org);
		} else {
			return org.equals(comp);
		}
	}

	/**
	 *  문자열의 대소비교
	 *
	 *  StringUtils.compare(null, null)   = 0
	 *  StringUtils.compare(null , "a")   < 0
	 *  StringUtils.compare("a", null)    > 0
	 *  StringUtils.compare("abc", "abc") = 0
	 *  StringUtils.compare("a", "b")     < 0
	 *  StringUtils.compare("b", "a")     > 0
	 *  StringUtils.compare("a", "B")     > 0
	 *  StringUtils.compare("ab", "abc")  < 0
	 *
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static int compare(final String str1, final String str2) {
		return compare(str1, str2, true);
	}

	/**
	 *  문자열의 대소비교
	 *
	 *  StringUtils.compare(null, null, *)     = 0
	 *  StringUtils.compare(null , "a", true)   < 0
	 *  StringUtils.compare(null , "a", false)  > 0
	 *  StringUtils.compare("a", null, true)    > 0
	 *  StringUtils.compare("a", null, false)   < 0
	 *  StringUtils.compare("abc", "abc", *)   = 0
	 *  StringUtils.compare("a", "b", *)       < 0
	 *  StringUtils.compare("b", "a", *)       > 0
	 *  StringUtils.compare("a", "B", *)       > 0
	 *  StringUtils.compare("ab", "abc", *)    < 0
	 *
	 * @param str1 문자열
	 * @param str2 비교대상 문자열
	 * @param nullIsLess NULL값이 작은지 여부
	 * @return
	 */
	public static int compare(final String str1, final String str2, final boolean nullIsLess) {
		if (str1 == str2) { // NOSONARLINT this intentionally uses == to allow for both null
			return 0;
		}
		if (str1 == null) {
			return nullIsLess ? -1 : 1;
		}
		if (str2 == null) {
			return nullIsLess ? 1 : -1;
		}
		return str1.compareTo(str2);
	}

	/**
	 * 문자열이 비어있을 경우 기본값을 반환한다.
	 *
	 * StringUtils.nvl(null, "love") == "love"
	 * StringUtils.nvl("nice", "love") == "nice"
	 *
	 * @param str1 문자열
	 * @param str2 기본값
	 * @return
	 */
	public static String nvl(String str1, String str2) {
		return str1 != null ? str1 : str2;
	}

	public static String nvl2(String str1, String str2, String str3) {
		return str1 != null ? str2 : str3;
	}

	/**
	 * 문자열이 비었는지 체크
	 *
	 * StringUtils.isEmpty(null)      = true
	 * StringUtils.isEmpty("")        = true
	 * StringUtils.isEmpty(" ")       = false
	 * StringUtils.isEmpty("bob")     = false
	 * StringUtils.isEmpty("  bob  ") = false
	 *
	 * @param str 문자열
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 문자열이 비어있지 않은지 체크
	 *
	 * StringUtils.isNotEmpty(null)      = false
	 * StringUtils.isNotEmpty("")        = false
	 * StringUtils.isNotEmpty(" ")       = true
	 * StringUtils.isNotEmpty("bob")     = true
	 * StringUtils.isNotEmpty("  bob  ") = true
	 *
	 * @param str 문자열
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 문자열의 왼쪽 공백을 제거
	 *
	 * StringUtils.ltrim(null)       ==  null
	 * StringUtils.ltrim("    test")       == "test"
	 * StringUtils.ltrim("test   ")        == "test   "
	 * StringUtils.ltrim("    test     ")  == "test     "
	 *
	 * @param str 문자열
	 * @return
	 */
	public static String ltrim(String str) {
		if (isEmpty(str))
			return str;
		return str.replaceAll("^\\s+", "");
	}

	/**
	 * 문자열의 오른쪽 공백을 제거
	 *
	 * StringUtils.ltrim(null)       ==  null
	 * StringUtils.ltrim("    test")       == "    test"
	 * StringUtils.ltrim("test   ")        == "test"
	 * StringUtils.ltrim("    test     ")  == "    test"
	 *
	 * @param str 문자열
	 * @return
	 */
	public static String rtrim(String str) {
		if (isEmpty(str))
			return str;
		return str.replaceAll("\\s+$", "");
	}


	/**
	 * 문자열의 좌우 공백 공백을 제거
	 *
	 * StringUtils.ltrim(null)       ==  null
	 * StringUtils.ltrim("    test")       == "test"
	 * StringUtils.ltrim("test   ")        == "test"
	 * StringUtils.ltrim("    test     ")  == "test"
	 *
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		if (isEmpty(str))
			return str;
		return str.trim();
	}

	/**
	 * 문자열의 모든 공백 제거
	 *
	 * StringUtils.trimAll(null)       ==  null
	 * StringUtils.trimAll("    te   st")       == "test"
	 * StringUtils.trimAll("te   st   ")        == "test"
	 * StringUtils.trimAll("    t    est     ")  == "test"
	 *
	 * @param str 문자열
	 * @return
	 */
	public static String trimAll(String str) {
		if (isEmpty(str))
			return str;
		return str.replaceAll("\\s+", "");
	}


	/**
	 * 기준문자열의 길이가 주어진 문자길이와 같은지 체크
	 *
	 * StringUtils.equalsLength("ABC", 3) = true
	 * StringUtils.equalsLength("ABC", 4) = false
	 * StringUtils.equalsLength(null, 4) = false
	 *
	 * @param str1 기준문자열
	 * @param length 문자길이
	 * @return
	 */
	public static boolean equalsLength(String str1, int length) {
		if (str1 == null)
			return false;
		return str1.length() == length;
	}

	/**
	 * 문자열배열을 연결하여 하나의 문자열로 생성
	 *
	 * @param strs 문자열 배열
	 * @return
	 */
	public static String concat(String... strs) {
		if (strs == null || strs.length == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (String str : strs) {
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * 주어진 문자열을 반복횟수만큼 반복하여 반환
	 * @param str 문자열
	 * @param count 반복횟수
	 * @return
	 */
	public static String repeat(String str, int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * 문자열 마스킹
	 *
	 * @param src      원본
	 * @param startIdx 시작위치
	 * @return 마스킹 적용된 문자열
	 */
	public static String mask(String src, int startIdx) {
		return mask(src, DEFAULT_REPLACE, null, startIdx, src.length());
	}

	/**
	 * 문자열 마스킹
	 *
	 * @param src      원본
	 * @param startIdx 시작위치
	 * @param length   길이
	 * @return 마스킹 적용된 문자열
	 */
	public static String mask(String src, int startIdx, int length) {
		return mask(src, DEFAULT_REPLACE, null, startIdx, length);
	}

	/**
	 * 문자열 마스킹
	 *
	 * @param src      원본
	 * @param replace  대치문자
	 * @param startIdx 시작위치
	 * @return 마스킹 적용된 문자열
	 */
	public static String mask(String src, char replace, int startIdx) {
		return mask(src, replace, null, startIdx, src.length());
	}

	/**
	 * 문자열 마스킹
	 *
	 * @param src      원본
	 * @param replace  대치문자
	 * @param startIdx 시작위치
	 * @param length   길이
	 * @return 마스킹 적용된 문자열
	 */
	public static String mask(String src, char replace, int startIdx, int length) {
		return mask(src, replace, null, startIdx, length);
	}

	/**
	 * 문자열 마스킹
	 *
	 * @param src      원본
	 * @param replace  대치문자
	 * @param exclude  제외문자
	 * @param startIdx 시작위치
	 * @param length   길이
	 * @return 마스킹 적용된 문자열
	 */
	public static String mask(String src, char replace, char[] exclude, int startIdx, int length) {
		StringBuffer sb = new StringBuffer(src);

		if (startIdx < 0) {
			int endIdx = sb.length() + startIdx - length;
			if (endIdx < 0)
				endIdx = 0;

			for (int i = sb.length() + startIdx; i > endIdx; i--) {
				replaceStringWithoutExcludes(sb, i, replace, exclude);
			}

			return sb.toString();
		} else {
			// 종료 인덱스
			int endIdx = startIdx + length;
			if (sb.length() < endIdx)
				endIdx = sb.length();

			// 치환
			for (int i = startIdx; i < endIdx; i++) {
				replaceStringWithoutExcludes(sb, i, replace, exclude);
			}

			return sb.toString();
		}
	}

	private static void replaceStringWithoutExcludes(StringBuffer sb, int i, char replace, char[] exclude) {
		boolean isExclude = false;
		// 제외 문자처리
		if (exclude != null && 0 < exclude.length) {
			char currentChar = sb.charAt(i);

			for (char excludeChar : exclude) {
				if (currentChar == excludeChar)
					isExclude = true;
			}
		}

		if (!isExclude)
			sb.setCharAt(i, replace);
	}

	/**
	 * 주어진 패딩문자열로 왼쪽으로 길이만큼 채운다.
	 * @param str 문자열
	 * @param length 길이
	 * @param padStr 패딩 문자열
	 * @return
	 */
	public static String lpad(String str, int length, String padStr) {
		if (str == null)
			return null;
		int pads = length - str.length();
		if (pads > 0) {
			return repeat(padStr, pads).concat(str);
		} else {
			return str;
		}
	}

	/**
	 * 주어진 패딩문자열로 오른쪽으로 길이만큼 채운다.
	 * @param str 문자열
	 * @param length 길이
	 * @param padStr 패딩 문자열
	 * @return
	 */
	public static String rpad(String str, int length, String padStr) {
		if (str == null)
			return null;
		int pads = length - str.length();
		if (pads > 0) {
			return str.concat(repeat(padStr, pads));
		} else {
			return str;
		}
	}

	/**
	 * 대상문자열에서 찾은 문자열을 변경
	 * @param text 대상문자열
	 * @param replacement 변경할문자열
	 * @param strs 찾을문자열
	 * @return
	 */
	public static String replace(String text, String replacement, String... strs) {
		if (text == null || strs == null) {
			return null;
		}
		for (String str : strs) {
			text = text.replace(str, replacement);
		}
		return text;
	}

	/**
	 * 대상문자열에서 정규표현식에 해당하는 문자를 변경
	 * @param str 대상문자열
	 * @param regex 정규표현식
	 * @param replacement 변경할문자
	 * @return
	 */
	public static String replaceGroup(String str, String regex, String replacement) {
		if (isEmpty(str) || isEmpty(regex) || isEmpty(replacement))
			return str;
		Pattern p = Pattern.compile(regex);
		return p.matcher(str).replaceAll(replacement);
	}

	/**
	 * 문자열 마스킹, 제외 문자열을 건너뛰고 마스킹 문자 개수만큼 반복
	 * @param str 원본 문자열
	 * @param count 마스킹할 문자열 개수
	 * @param excludes 제외 문자열
	 * @param direction 앞에서부터 1, 뒤에서부터 0
	 * @return
	 */
	public static String maskByRepeat(String str, int count, char replace, char[] excludes, int direction) {
		StringBuilder sb = new StringBuilder(str);

		if (direction == 1) {
			// 정방향
			for (int i = 0; i < str.length(); i++) {
				boolean isExclude = false;
				if (count == 0) {
					break;
				}

				char currentChar = sb.charAt(i);
				for (char excludeChar : excludes) {
					if (currentChar == excludeChar) {
						isExclude = true;
						break;
					}
				}

				if (!isExclude) {
					sb.setCharAt(i, replace);
					count--;
				}
			}
		} else {
			// 역방향
			for (int i = str.length() - 1; i > 0; i--) {
				boolean isExclude = false;
				if (count == 0) {
					break;
				}

				char currentChar = sb.charAt(i);
				for (char excludeChar : excludes) {
					if (currentChar == excludeChar) {
						isExclude = true;
						break;
					}
				}

				if (!isExclude) {
					sb.setCharAt(i, replace);
					count--;
				}
			}
		}

		return sb.toString();
	}


	/**
	 * 문자열이 표현식에 일치하는지 체크
	 * @param str 문자열
	 * @param regex 표현식
	 * @return
	 */
	public static boolean matches(String str, String regex){
		if( isEmpty(str)) return false;
		return str.matches(regex);
	}

	/**
	 * 오른쪽으로 부터 정해진 숫자만큼 자른 값을 반환 부족할경우 문자열로 채워서 반환
	 * @param str
	 * @param size 길이
	 * @param padStr 크기가 부족할 경우 채울 문자열
	 * @return
	 */
	public static String radj(String str, int size, String padStr){
		if( str == null) throw new IllegalArgumentException("str is null");
		if( str.length() == size) return str;

		if(str.length() > size){
			return str.substring(str.length() - size, str.length());
		}else{
			return rpad(str, size, padStr);
		}
	}

	/**
	 * 왼쪽으로 부터 정해진 숫자만큼 자른 값을 반환 부족할경우 문자열로 채워서 반환
	 * @param str
	 * @param size 길이
	 * @param padStr 크기가 부족할 경우 채울 문자열
	 * @return
	 */
	public static String ladj(String str, int size, String padStr){
		if( str == null) throw new IllegalArgumentException("str is null");
		if( str.length() == size) return str;

		if(str.length() > size){
			return str.substring(0, size);
		}else{
			return lpad(str, size, padStr);
		}
	}

	/**
	 * 문자열이 영어인지 여부를 반환한다.
	 *
	 *  StringUtils.isAlpha("alpha")       == true
	 *  StringUtils.isAlpha("alp ha")      == false
	 *  StringUtils.isAlpha("alpha123")    == false
	 *
	 * @param str 문자열
	 * @return 문자열이 영어라면 true 그렇지 않다면 false를 반환한다.
	 */
	public static boolean isAlpha(String str){
		return Pattern.matches("^[a-zA-Z]*$", str);
	}

	/**
	 * 문자열이 영어인지 여부를 반환한다.
	 *
	 *  StringUtils.isNumeric("123123")    == true
	 *  StringUtils.isNumeric("123a")      == false
	 *  StringUtils.isNumeric("123 12 ")   == false
	 *  StringUtils.isNumeric(" 12312 ")   == false
	 *
	 * @param str 문자열
	 * @return 문자열이 숫자라면 true 그렇지 않다면 false를 반환한다.
	 */
	public static boolean isNumeric (String str){
		return Pattern.matches("^[0-9]*$", str);
	}

	/**
	 * 문자열이 영어와 숫자가 결합되어 있는지 여부를 반환한다.
	 *
	 *  StringUtils.isAlphaNumeric("al123pha") == true
	 *  StringUtils.isAlphaNumeric("12512")    == false
	 *  StringUtils.isAlphaNumeric("asdfbafg") == false
	 *  StringUtils.isAlphaNumeric("asdf 123") == false
	 *
	 * @param str 문자열
	 * @return 문자열이 영어와 숫자가 결합된 형태면 true 그렇지 않다면 false를 반환한다.
	 */
	public static boolean isAlphaNumeric (String str){
		return !isAlpha(str) && !isNumeric(str) && Pattern.matches("^[A-Za-z0-9]*$", str);
	}

	/**
	 * 문자열이 공백인지 여부를 반환한다.
	 *
	 *  StringUtils.isWhitespace("      ") == true
	 *  StringUtils.isWhitespace("1 ")     == false
	 *  StringUtils.isWhitespace(" a")     == false
	 *  StringUtils.isWhitespace("a")      == false
	 *  StringUtils.isWhitespace("1 a")    == false
	 *
	 * @param str 문자열
	 * @return 문자열이 공백이라면 true 그렇지 않다면 false를 반환한다.
	 */
	public static boolean isWhitespace (String str){
		return Pattern.matches("^[ \\t\\r\\n\\v\\f]*$",str);
	}

	/**
	 * 문자열이 한글인지 여부를 반환한다.
	 *
	 *  StringUtils.isHangeul("가나다")    == true
	 *  StringUtils.isHangeul("가 나다")  == false
	 *  StringUtils.isHangeul("가1나다")    == false
	 *  StringUtils.isHangeul("가a나다")    == false
	 *
	 * @param str 문자열
	 * @return 문자열이 한글이라면 true 그렇지 않다면 false를 반환한다
	 */
	public static boolean isHangeul(String str){
		return Pattern.matches("^[ㄱ-ㅎ가-힣]*$",str);
	}

	/**
	 * 문자열이 한글과 숫자가 결합되어있는지 여부를 반환한다.
	 *  StringUtils.isHangeulNumeric("가나다1")    == true
	 *  StringUtils.isHangeulNumeric("가 나다")  == false
	 *  StringUtils.isHangeulNumeric("가1나다")    == true
	 *  StringUtils.isHangeulNumeric("가a나다")    == false
	 * @param str
	 * @return
	 */
	public static boolean isHangeulNumeric(String str){
		return !isHangeul(str) && !isNumeric(str) && Pattern.matches("^[ㄱ-ㅎ가-힣0-9 ]*$",str);
	}

	/**
	 * 문자열에 찾는 문자가 포함되어있는지 여부
	 *
	 * assertTrue(StringUtils.contains("ABC", "A"));
	 * assertFalse(StringUtils.contains("BC", "A"));
	 *
	 * @param str 문자열
	 * @param s 찾는 문자
	 * @return
	 */
	public static boolean contains(String str, String s) {
		if( StringUtils.isAnyEmpty(str, s)) return false;
		return str.contains(s);
	}

	/**
	 * 문자열을 자르고 인덱스 부분을 가져옴
	 * assertEquals("A", StringUtils.splitAt("A,B,C", ",", 0));
	 * assertEquals("B", StringUtils.splitAt("A,B,C", ",", 1));
	 * assertEquals("C", StringUtils.splitAt("A,B,C", ",", 2));
	 * assertNull(StringUtils.splitAt("A,B,C", ",", 3));
	 * @param str 문자열
	 * @param delimiter 구분자
	 * @param index 추출할 인덱스
	 * @return
	 */
	public static String splitAt(String str, String delimiter, int index) {
		if(isAnyEmpty(str, delimiter)) return null;

		String[] arr = str.split(delimiter);
		if( index  > arr.length - 1) return null;
		else return arr[index];
	}

	/**
	 * 문자열 전체가 비어있는지 체크
	 * assertFalse(StringUtils.isAllEmpty("", "TSET"));
	 * assertFalse(StringUtils.isAllEmpty(null, "TSET"));
	 * assertFalse(StringUtils.isAllEmpty("MAN", "TSET"));
	 * assertTrue(StringUtils.isAllEmpty("", null));
	 * @param strs 문자열
	 * @return
	 */
	public static boolean isAllEmpty(String... strs) {
		if( strs == null) return false;
		return Arrays.stream(strs).allMatch(str -> isEmpty(str));
	}

	/**
	 * 문자열 한개라도 비어있는지 체크
	 * assertTrue(StringUtils.isAnyEmpty("", "TSET"));
	 * assertTrue(StringUtils.isAnyEmpty(null, "TSET"));
	 * assertFalse(StringUtils.isAnyEmpty("MAN", "TSET"));
	 * @param strs 문자열
	 * @return
	 */
	public static boolean isAnyEmpty(String... strs) {
		if( strs == null) return false;
		return Arrays.stream(strs).anyMatch(str -> isEmpty(str));
	}

	/**
	 * 문자열의 바이트 길이를 반환한다.
	 * @param s
	 * @param charsetName
	 * @return
	 */
	public static int getByteLength(String s, String charsetName) {
		if( isEmpty(s)) return 0;
		return toByteArray(s, charsetName).length;
	}

	/**
	 * 문자열을 바이트 배열로 반환 한다.
	 * @param str 문자열
	 * @return
	 */
	public static byte[] toByteArray(String str) {
		return toByteArray(str, DEFAULT_CHAR_SET);
	}

	/**
	 * 문자열을 바이트 배열로 반환 한다.
	 * @param str 문자열
	 * @param charsetName 캐릭터셋명
	 * @return
	 */
	public static byte[] toByteArray(String str, String charsetName) {
		try {
			return str.getBytes(charsetName);
		} catch (UnsupportedEncodingException e) {
		}

		return str.getBytes();
	}

	/**
	 * <p>
	 * 문자열을 주어진 length byte 수 만큼 문자열을 잘라서 반환한다.
	 * </p>
	 *
	 * <pre>
	 * StringUtils.byteSubstring("UTF-8", "서울2가", 3)) = 서
	 * StringUtils.byteSubstring("UTF-8", "서울2가", 5)) = 서
	 * StringUtils.byteSubstring("EUC-KR", "서울2가", 5)) = 서울2
	 * </pre>
	 *
	 * @author Kang Seok Han
	 * @param charsetName
	 * @param data 문자열
	 * @param length byte수
	 * @return
	 */
	public static String byteSubstring(String charsetName, String data, int length) {
		Charset charset = Charset.forName(charsetName);
		CharsetDecoder decoder = charset.newDecoder();

		ByteBuffer byteBuf = ByteBuffer.wrap(data.getBytes(charset), 0, length);
		CharBuffer charBuf = CharBuffer.allocate(length);

		decoder.onMalformedInput(CodingErrorAction.IGNORE);
		decoder.decode(byteBuf, charBuf, true);
		decoder.flush(charBuf);

		return new String(charBuf.array(), 0, charBuf.position());
	}



}