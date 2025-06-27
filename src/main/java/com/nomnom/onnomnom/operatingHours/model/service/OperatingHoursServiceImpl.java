package com.nomnom.onnomnom.operatingHours.model.service;

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
import com.nomnom.onnomnom.global.exception.OperatingHoursEnrollFailedException;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;
import com.nomnom.onnomnom.operatingHours.model.dto.OperatingHoursDTO;
import com.nomnom.onnomnom.operatingHours.model.vo.OpeningHoursVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OperatingHoursServiceImpl implements OperatingHoursService {
	
	private final ResponseWrapperService responseWrapperService;
	
	@Override
	public ObjectResponseWrapper<String> insertOperatingHours(List<OperatingHoursDTO> operatingHours) throws ParseException {
		
		int count = 0;
		
		for(OperatingHoursDTO operatingHoursInfo : operatingHours) {
			
			String open = operatingHoursInfo.getStartTime();
			String close = operatingHoursInfo.getEndTime();
			String breakStart = operatingHoursInfo.getBreakStartTime();
			String breakEnd = operatingHoursInfo.getBreakEndTime();
			String restaurantNo = operatingHoursInfo.getRestaurantNo();
			String weekDay = operatingHoursInfo.getWeekDay();
			
			if(!open.isEmpty() && !close.isEmpty()) {
				
				count+=1;
				
				checkMinutes(open);
				checkMinutes(close);
				
				String endTime = (calculateTime(open,close) == 2) ? plusTime(close) : close;    
				OpeningHoursVo openingHoursVo = getOpeningHoursVo(restaurantNo,weekDay,open,endTime);
			
				// 운영시각 insert
				
				if(!breakStart.isEmpty() && !breakEnd.isEmpty()) {
					checkMinutes(breakStart);
					checkMinutes(breakEnd);
					
					String breakEndTime = (calculateTime(breakStart,breakEnd) == 2) ? plusTime(breakEnd) : breakEnd;    
					breakExceptionHandler(open,endTime,breakStart,breakEndTime);
					OpeningHoursVo breakTimeVo = getBreakTimeVo(breakStart,breakEndTime);
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
	    else throw new RuntimeException("10분 단위로 입력해주세요");
	}
	
	private int calculateTime(String firstTime, String secondTime) throws ParseException {
		LocalTime startTime = stringToLocalTimeFormatter(firstTime);
		LocalTime endTime = stringToLocalTimeFormatter(secondTime);
		return Duration.between(startTime, endTime).toMinutes() > 0 ? 1 : 2; 
	}
	
	private String plusTime(String time) {
	    int originalHour = Integer.parseInt(time.split(":")[0]);
	    int minute = Integer.parseInt(time.split(":")[1]);
	    int newHour = originalHour + 24;
	    return String.format("%02d:%02d", newHour, minute);
	}
	
	private LocalTime stringToLocalTimeFormatter(String time)  {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
		LocalTime localTime =  LocalTime.parse(time, formatter);
		return localTime;
	}
	
	private OpeningHoursVo getOpeningHoursVo(String restaurantNo,String weekDay,String open,String close) {
		OpeningHoursVo openingHoursVo = OpeningHoursVo.builder()
				.restaurantNo(restaurantNo)
				.weekDay(weekDay)
				.startTime(open)
				.endTime(close)
				.build();
		return openingHoursVo;
	}
	
	private OpeningHoursVo getBreakTimeVo(String open,String close) {
		OpeningHoursVo breakTimeVo = OpeningHoursVo.builder()
				.breakStartTime(open)
				.breakEndTime(close)
				.build();
		return breakTimeVo;
	}
	
	private void breakExceptionHandler(String open,String endTime,String breakStart,String breakEndTime) {
		try {
			if(calculateTime(open,breakStart) ==2) {
				throw new BreakTimeException(ErrorCode.BREAK_TIME_RANGE_ERROR);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(calculateTime(breakEndTime,endTime) ==2) {
				throw new BreakStartTimeException(ErrorCode.BREAK_STARTTIME_ERROR);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			if(calculateTime(breakStart,breakEndTime) ==2) {
				throw new BreakEndTimeException(ErrorCode.BREAK_ENDTIME_ERROR);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
			
	}
	
	
	
}
