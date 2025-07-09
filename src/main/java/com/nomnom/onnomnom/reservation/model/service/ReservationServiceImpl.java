package com.nomnom.onnomnom.reservation.model.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.nomnom.onnomnom.auth.model.service.AuthService;
import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.NonExistingTimeException;
import com.nomnom.onnomnom.global.exception.UnavailableReservationException;
import com.nomnom.onnomnom.global.response.ListResponseWrapper;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;
import com.nomnom.onnomnom.operating.model.vo.OperatingVo;
import com.nomnom.onnomnom.reservation.model.dao.ReservationMapper;
import com.nomnom.onnomnom.reservation.model.dto.ReservationDTO;
import com.nomnom.onnomnom.reservation.model.dto.ResponseResultDTO;
import com.nomnom.onnomnom.reservation.model.dto.ResponseTimeDTO;
import com.nomnom.onnomnom.reservation.model.vo.ReservationVo;
import com.nomnom.onnomnom.reservationSetting.model.dto.ReservationSettingDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
	
	private final ResponseWrapperService responseWrapperService;
	private final ReservationMapper reservationMapper;
	private final AuthService authService;
	
	@Override
	public ObjectResponseWrapper<String> insertReservation(ReservationDTO reservationDTO) {
		/*
		 * restauratnNo,reserveDay,reserveTime로 조회했을 때 조회된 행이 MAX_TEAM_NUM보다 작으면 insert하기
		 */
		CustomUserDetails memeber = authService.getUserDetails();
		String memberNo = memeber.getMemberNo();
		ReservationVo reservationVo = ReservationVo.builder()
				.restaurantNo(reservationDTO.getRestaurantNo())
				.memberNo(memberNo)
				.reserveDay(reservationDTO.getReserveDay())
				.reserveTime(reservationDTO.getReserveTime())
				.numberOfGuests(reservationDTO.getNumberOfGuests())
				.build();
		ReservationDTO reservationCheck = reservationMapper.selectCheckAvailableTimes(reservationVo);
		if(reservationCheck != null ) {
			// 예외처리
			throw new UnavailableReservationException(ErrorCode.UNAVAILABLE_RESERVATION_ERROR);
		}
								
		int result = reservationMapper.insertReservationInfo(reservationVo);
		
		return responseWrapperService.wrapperCreate("S101", "예약 성공");
	}
	
	@Override
	public ObjectResponseWrapper<ReservationSettingDTO> selectReservationInfo(String restaurantNo) {
		// 최대 최소 인원수, 설명 조회
		ReservationSettingDTO reservationInfo = reservationMapper.selectReservationInfo(restaurantNo);
		return responseWrapperService.wrapperCreate("S101", "예약설정 정보 성공",reservationInfo);
	}


	
	@Override
	public ObjectResponseWrapper<ResponseResultDTO> selectReservation(String restaurantNo, String reserveDay) {
		/*
		 * 선택한 날짜와 식당 번호로 1. 예약이 가능한 시간대와, 2. 예약이 가능한 시간대 중에 예약이 차지 않은 시간대들을 반환해야함
		 * 
		 * 1. 예약 가능한 시간대 => 예약시작,마감시각/브레이크 시작,마감시각/시간간격으로  계산
		 * 2. 그 시간대로 db조회해서 예약 가능한지 아닌지(boolean) 반환
		 * LIST<예약시간> = 시간, 예약가능(boolean)
		 * 시간 = 가게정보 
		 * 예약 가능 = 예약 테이블 
		 */
		/*
		 * 1-1. RESERVE_DAY에서 요일 추출
		 * 1-2. weekday,restaurantNo으로 db 조회해서 브레이크정보,예약가능한시간정보,예약간격 얻어오기
		 * 		(브레이크 타임은 없을 수도 있음)
		 * 1-3. 그걸 이용해서 예약이 가능한 시간대 리스트로 조회
		 * 2-1. 조회된 시간 리스트로 for문 돌려서 TB_RESERVATION_LIST테이블에 예약이 가능한지 확인
		 */
		log.info("reserveDay : {}",reserveDay);
		log.info("restaurantNo : {}",restaurantNo);
		String weekDay = getDayOfWeek(reserveDay);
		OperatingVo operatingVo = OperatingVo.builder()
	              .restaurantNo(restaurantNo)
	              .weekDay(weekDay)
	              .build();
		ResponseTimeDTO timesInfo = reservationMapper.selectTimesInfo(operatingVo);
		log.info("timesInfo : {}",timesInfo);
		if(timesInfo == null) {
			throw new NonExistingTimeException(ErrorCode.NON_EXISTING_TIME_ERROR);
		}
		String breakStartTime = timesInfo.getBreakStartTime();
		String breakEndTime = timesInfo.getBreakEndTime();
		String reservationStartTime = timesInfo.getReservationStartTime();
		String reservationEndTime = timesInfo.getReservationEndTime();
		int interval = timesInfo.getInterval();
		
		List<String> times = new ArrayList<String>();
		if(breakStartTime != null && breakEndTime != null) {
			times = availableTimes(breakStartTime,breakEndTime,reservationStartTime,reservationEndTime,interval);
		} else {
			times = availableTimes(reservationStartTime,reservationEndTime,interval);
		}
		ResponseResultDTO responseDTO = new ResponseResultDTO();
		Map<String, Boolean> resultMap = new HashMap<>();
		for(String time : times) {
			ReservationVo reservationVo = ReservationVo.builder()
					.restaurantNo(restaurantNo)
					.reserveDay(reserveDay)
					.reserveTime(time)
					.build();
			ReservationDTO reservationDTO = reservationMapper.selectCheckAvailableTimes(reservationVo);
			
			// 이거 조회 값이 나오면 예약 가득찬거임
			if(reservationDTO != null ) {
				
				resultMap.put(reservationDTO.getReserveTime(),false);
			} else {
				resultMap.put(time,true);
			}
			responseDTO.setResultMap(resultMap);	
		}
		log.info("responseDTO : {}",responseDTO);
		return responseWrapperService.wrapperCreate("S101", "예약 가능한 시각 성공",responseDTO);
	}
	
	private String getDayOfWeek(String date) {
        LocalDate localDate = LocalDate.parse(date);
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        String weekDay = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        return weekDay;
	}

	private List<String> availableTimes(String breakStart,String breakEnd,String reserveStart,String reserveEnd,int timeInterval) {
		List<String> times = new ArrayList<String>();
		int reserveStartMinute = toMinute(reserveStart);
		int reserveEndMinute = toMinute(reserveEnd);
		int breakStartMinute = toMinute(breakStart);
		int breakEndMinute = toMinute(breakEnd);
		while (reserveStartMinute < reserveEndMinute) {
			if(reserveStartMinute < breakStartMinute || reserveStartMinute > breakEndMinute ) {
				 times.add(toTimeString(reserveStartMinute));
			  };
			reserveStartMinute += timeInterval;
		 	}; 
		return times;
	}
	
	private List<String> availableTimes(String reserveStart,String reserveEnd,int timeInterval) {
		List<String> times = new ArrayList<String>();
		int reserveStartMinute = toMinute(reserveStart);
		int reserveEndMinute = toMinute(reserveEnd);
		while (reserveStartMinute < reserveEndMinute) {
			 times.add(toTimeString(reserveStartMinute));
			reserveStartMinute += timeInterval;
		 }; 
		return times;
	}
	
	
	
	private int toMinute(String timeStr) {
	    String[] split = timeStr.split(":");
	    int hour   = Integer.parseInt(split[0]);  
	    int minute = Integer.parseInt(split[1]);   
	    return hour * 60 + minute;
	}
	
	private String toTimeString(int totalMinutes) {
	    int hour   = totalMinutes / 60;
	    int minute = totalMinutes % 60;
	    return String.format("%02d:%02d", hour, minute);
	}
	

	@Override
	public ListResponseWrapper<ReservationDTO> selectReservationCheck(String restaurantNo) {
		CustomUserDetails memeber = authService.getUserDetails();
		String memberNo = memeber.getMemberNo();
		ReservationVo reservationVo = ReservationVo.builder()
													.memberNo(memberNo)
													.build();
		List<ReservationDTO> myReservation = reservationMapper.selectReservationCheck(reservationVo);
		return responseWrapperService.wrapperCreate("S101", "내 예약내역 조회 성공",myReservation);
	}
	
	@Override
	public ListResponseWrapper<ReservationDTO> selectAllReservation() {
		List<ReservationDTO> allReservation = reservationMapper.selectAllReservation();
		return responseWrapperService.wrapperCreate("S101", "모든 예약 조회 성공",allReservation);
	}
	
	
	@Override
	public ObjectResponseWrapper<String> deleteReservation(String reservationNo) {
		int deleteResult = reservationMapper.deleteReservation(reservationNo);
		return responseWrapperService.wrapperCreate("S101", "예약 삭제 성공");
	}







}
