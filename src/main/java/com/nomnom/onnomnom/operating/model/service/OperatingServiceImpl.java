package com.nomnom.onnomnom.operating.model.service;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.BreakEndTimeException;
import com.nomnom.onnomnom.global.exception.BreakStartTimeException;
import com.nomnom.onnomnom.global.exception.BreakTimeException;
import com.nomnom.onnomnom.global.exception.DeleteOperatingInfoFailedException;
import com.nomnom.onnomnom.global.exception.ExistingOperatingHoursException;
import com.nomnom.onnomnom.global.exception.OperatingHoursEnrollFailedException;
import com.nomnom.onnomnom.global.exception.OperatingHoursUpdateFailedException;
import com.nomnom.onnomnom.global.exception.TimeValueException;
import com.nomnom.onnomnom.global.response.ListResponseWrapper;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;
import com.nomnom.onnomnom.operating.model.dao.OperatingMapper;
import com.nomnom.onnomnom.operating.model.dto.OperatingDTO;
import com.nomnom.onnomnom.operating.model.vo.OperatingVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OperatingServiceImpl implements OperatingService {
	
	private final ResponseWrapperService responseWrapperService;
	private final OperatingMapper operatingMapper;
	
	/* 1. 예외처리 미리 하기 => int count 없애기
	 * 2. 10분단위 확인 다시/ 함수호출 한번만
	 * 3. 24시간더하기 앞에서 하기
	 * 4. 트랜잭션 처리 => 애노테이션?
	 * 5. 메서드 따로 빼서 예외처리 다 하기
	 */
	
	@Override
	public ObjectResponseWrapper<String> insertOperating(List<OperatingDTO> operatingHours) throws ParseException {
		
		int count = 0;
		
		log.info("operatingHours : {}",operatingHours);
		
	
		
		for(OperatingDTO operatingHoursInfo : operatingHours) {
			
			String open = operatingHoursInfo.getStartTime();
			String close = operatingHoursInfo.getEndTime();
			String breakStart = operatingHoursInfo.getBreakStartTime();
			String breakEnd = operatingHoursInfo.getBreakEndTime();
			
			if(operatingMapper.selectCountByRestaurantNo(operatingHoursInfo.getRestaurantNo()) != 0) {
				throw new ExistingOperatingHoursException(ErrorCode.EXISTING_OPERATING_HOURS_ERROR);
			};
			
			if(!open.isEmpty() && !close.isEmpty() && open != null && close != null) {
				
				count+=1;
				checkMinutes(open);
				checkMinutes(close);
				OperatingVo operatingVo = getOperatingVo(operatingHoursInfo.getRestaurantNo(),operatingHoursInfo.getWeekDay(),open,close);
			
				// 운영시각 insert
				int operatingInsertResult = operatingMapper.insertOperatingHours(operatingVo);
				
				System.out.println(operatingVo.getOperatingHoursNo());
				
				if(!breakStart.isEmpty() && !breakEnd.isEmpty() && breakStart != null && breakEnd != null) {
					checkMinutes(breakStart);
					checkMinutes(breakEnd);
					
					breakExceptionHandler(open,close,breakStart,breakEnd);
					String operatingNo = operatingVo.getOperatingHoursNo();
					OperatingVo breakTimeVo = getBreakTimeVo(operatingNo,breakStart,breakEnd);
					int breakInsertResult = operatingMapper.insertBreaktime(breakTimeVo);
				}
			}
		}
		if(count ==0) {
			throw new OperatingHoursEnrollFailedException(ErrorCode.BUSINESS_INFO_REGISTER_FAIL);
		}
		return responseWrapperService.wrapperCreate("S100", "운영정보 등록 성공");
	}
	
	
	private int checkMinutes(String time) {
	    String[] parts = time.split(":");
	    int minutes = Integer.parseInt(parts[1]);
	    if (minutes % 10 == 0) return 1;
	    else throw new TimeValueException(ErrorCode.TIME_VALUE_ERROR);
	}
	
	private int calculateTime(String firstTime, String secondTime) throws ParseException {
		LocalTime startTime = stringToLocalTimeFormatter(firstTime);
		LocalTime endTime = stringToLocalTimeFormatter(secondTime);
		return Duration.between(startTime, endTime).toMinutes() > 0 ? 1 : 2; 
	}
	

	private LocalTime stringToLocalTimeFormatter(String time)  {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
		LocalTime localTime =  LocalTime.parse(time, formatter);
		return localTime;
	}
	
	private OperatingVo getOperatingVo(String restaurantNo,String weekDay,String open,String close) {
		OperatingVo operatingVo = OperatingVo.builder()
				.restaurantNo(restaurantNo)
				.weekDay(weekDay)
				.startTime(open)
				.endTime(close)
				.build();
		return operatingVo;
	}
	
	private OperatingVo getBreakTimeVo(String operatingHoursNo,String open,String close) {
		OperatingVo breakTimeVo = OperatingVo.builder()
				.operatingHoursNo(operatingHoursNo)
				.breakStartTime(open)
				.breakEndTime(close)
				.build();
		return breakTimeVo;
	}
	
	private void breakExceptionHandler(String open,String endTime,String breakStart,String breakEndTime) {
		exceptionHandler(open,breakStart,new BreakTimeException(ErrorCode.BREAK_TIME_RANGE_ERROR));
		exceptionHandler(open,breakStart,new BreakStartTimeException(ErrorCode.BREAK_STARTTIME_ERROR));
		exceptionHandler(open,breakStart,new BreakEndTimeException(ErrorCode.BREAK_ENDTIME_ERROR));
	}
	
	private void exceptionHandler(String first, String end, RuntimeException ex) {
		try {
			if(calculateTime(first,end) ==2) {
				throw ex;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ListResponseWrapper<OperatingDTO> selectOperating(String restaurantNo) {
		List<OperatingDTO> operatingInfo = operatingMapper.selectOperatingInfo(restaurantNo);
		log.info("operatingInfo: {}",operatingInfo);
		return responseWrapperService.wrapperCreate("S102", "운영정보 조회 성공",operatingInfo);
	}

	@Override
	public ObjectResponseWrapper<String> deleteOperating(String restaurantNo) {
		int deleteBreaktime = operatingMapper.deleteBreaktimeInfo(restaurantNo);
		int deleteOperating = operatingMapper.deleteOperatingInfo(restaurantNo);
		if(deleteOperating == 0) {
			throw new DeleteOperatingInfoFailedException(ErrorCode.DELETE_OPERATING_INFO_FAILED_ERROR);
		}
		return responseWrapperService.wrapperCreate("S104", "운영정보 삭제 성공");
	}


	@Override
	public ObjectResponseWrapper<String> updateOperating(List<OperatingDTO> operatingHours) throws ParseException {
		
		int count = 0;
		
		for(OperatingDTO operatingHoursInfo : operatingHours) {
			
			String open = operatingHoursInfo.getStartTime();
			String close = operatingHoursInfo.getEndTime();
			String breakStart = operatingHoursInfo.getBreakStartTime();
			String breakEnd = operatingHoursInfo.getBreakEndTime();
			
			if(!open.isEmpty() && !close.isEmpty() && open != null && close != null) {
				
				count+=1;
				
				checkMinutes(open);
				checkMinutes(close);
				
				OperatingVo operatingVo = getOperatingVo(operatingHoursInfo.getRestaurantNo(),operatingHoursInfo.getWeekDay(),open,close);
				
				// 운영시각 insert
				int result = operatingMapper.findOperatingHours(operatingVo);
				int operatingInsertResult = result == 0 ? operatingMapper.insertOperatingHours(operatingVo) : 
															operatingMapper.updateOperatingHours(operatingVo);
				
				if(!breakStart.isEmpty() && !breakEnd.isEmpty() && breakStart != null && breakEnd != null) {
					checkMinutes(breakStart);
					checkMinutes(breakEnd);
					
					breakExceptionHandler(open,close,breakStart,breakEnd);
					String operatingNo = result == 0 ? operatingVo.getOperatingHoursNo() : operatingMapper.findOperatingNo(operatingVo);
					OperatingVo breakTimeVo = getBreakTimeVo(operatingNo,breakStart,breakEnd);
					int result1 = operatingMapper.findBreaktime(operatingVo);
					
					int breakInsertResult = result1 == 0 ? operatingMapper.insertBreaktime(breakTimeVo)
														 : operatingMapper.updateBreaktime(breakTimeVo);
				}
			}
		}
		if(count ==0) {
			throw new OperatingHoursUpdateFailedException(ErrorCode.BUSINESS_INFO_UPDATE_FAIL);
		}
		return responseWrapperService.wrapperCreate("S103", "운영정보 수정 성공");
	}

}
