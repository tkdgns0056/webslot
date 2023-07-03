package com.example.slotweb.extend;


import java.util.List;
import java.util.Map;

public interface ExcelSupport {

	/**
	 * 시트 카운트
	 * 여러 시트를 렌더링 할 경우 지정
	 * @return
	 */
	default int getSheetCount() {
		return 1;
	}

	/**
	 * 업로드, 엑셀데이터 처리
	 * @param list 엑셀 파싱 데이터
	 * @param newParam
	 * @param param 파라미터
	 * @return
	 */
	default void processExcelData(String uid, List<List<String>> list, Map<String, Object> newParam, Map<String, Object> result){}

	/**
	 * 다운로드, 엑셀 폼 데이터 처리
	 * @param uid 고유아이디
	 * @param param 파라미터
	 * @param data 엑셀에 셋팅할 데이터
	 */
	default void processExcelFromData(String uid, Map param, Map data){}


	/**
	 * 엑셀별 마지막 셀번호를 지정
	 * @param uid
	 * @return
	 */
	default int getLastCell(String uid){ return 5;};

	/**
	 * 다운로드, 엑셀 데이터 처리
	 * @param uid
	 * @param newParam
	 * @param excelContext
	 */
	default void buildExcelData(String uid, Map newParam, ExcelContext excelContext){};
}
