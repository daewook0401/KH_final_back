package com.nomnom.onnomnom.reservationSetting.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.nomnom.onnomnom.auth.controller.AuthController;
import com.nomnom.onnomnom.auth.model.service.AuthService;
import com.nomnom.onnomnom.auth.model.vo.CustomUserDetails;
import com.nomnom.onnomnom.global.enums.ErrorCode;
import com.nomnom.onnomnom.global.exception.FailedToEnrollSettingException;
import com.nomnom.onnomnom.global.exception.NotEnrolledOperatingHours;
import com.nomnom.onnomnom.global.exception.TimeValueException;
import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.global.service.ResponseWrapperService;
import com.nomnom.onnomnom.reservation.model.dto.ReservationDTO;
import com.nomnom.onnomnom.reservation.model.service.ReservationService;
import com.nomnom.onnomnom.reservationSetting.model.dao.ReservationSettingMapper;
import com.nomnom.onnomnom.reservationSetting.model.dto.AvailableTimeDTO;
import com.nomnom.onnomnom.reservationSetting.model.dto.ReservationSettingDTO;
import com.nomnom.onnomnom.reservationSetting.model.dto.ReservationSettingRequestDTO;
import com.nomnom.onnomnom.reservationSetting.model.vo.AvailableTimeVo;
import com.nomnom.onnomnom.reservationSetting.model.vo.ReservationSettingVo;
import com.nomnom.onnomnom.restaurant.model.dto.RestaurantDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationSettingServiceImpl implements ReservationSettingService {

    private final ReservationSettingMapper reservationSettingMapper;
    private final ResponseWrapperService responseWrapperService;
    private final AuthService authService;
    
	@Override
	public ObjectResponseWrapper<String> insertSetting(ReservationSettingRequestDTO request) {
		
		List<AvailableTimeDTO> availableTime = request.getReservation();      
	    ReservationSettingDTO  settingInfo = request.getSettingInfo(); 
	    
	    // restaurantNo꺼내서 운영정보가 등록되어있지 않으면 예외처리 후 return하기
	    int operatingInfoCheck = reservationSettingMapper.selectCheckOperatingInfo(settingInfo.getRestaurantNo());
	    if(operatingInfoCheck == 0) {
	    	throw new NotEnrolledOperatingHours(ErrorCode.NOT_ENROLLED_OPERATING_HOURS_ERROR);
	    }
	    
	    
	    log.info("settingInfo : {} ",settingInfo);
	    log.info("availableTime : {} ",availableTime);
	    ReservationSettingVo reservationSettingVo = ReservationSettingVo.builder()
	    		.restaurantNo(settingInfo.getRestaurantNo())
	    		.interval(settingInfo.getInterval())
	    		.maxNum(settingInfo.getMaxNum())
	    		.minNum(settingInfo.getMinNum())
	    		.maxTeamNum(settingInfo.getMaxTeamNum())
	    		.description(settingInfo.getDescription())
	    		.build();
	    
	    int resul1t = reservationSettingMapper.insertSettingInfo(reservationSettingVo);
	    
	    
	    for(AvailableTimeDTO time : availableTime) {
	    	
	    	String reservationStartTime = time.getReservationStartTime();
	    	String reservationEndTime = time.getReservationEndTime();
	    	
	    	if(!reservationStartTime.isEmpty() && !reservationEndTime.isEmpty() && reservationStartTime != null && reservationEndTime != null) {
	    		checkMinutes(reservationStartTime);
	    		checkMinutes(reservationEndTime);
	    		
	    		
	    		AvailableTimeVo availableTimeVo = AvailableTimeVo.builder()
	    									.restaurantNo(time.getRestaurantNo())
	    									.weekDay(time.getWeekDay())
	    									.reservationStartTime(time.getReservationStartTime())
	    									.reservationEndTime(time.getReservationEndTime())
	    									.build();
	    		int checkOperatingHoursNo = reservationSettingMapper.selectOperatingHoursNo(availableTimeVo);
	    		
	    		if (checkOperatingHoursNo != 0) {
	    			
	    			int result = reservationSettingMapper.insertAvailableTime(availableTimeVo);
	    			if(result == 0) {
	    				reservationSettingMapper.deleteSettingInfo(settingInfo.getRestaurantNo());
	    				throw new FailedToEnrollSettingException(ErrorCode.FAILED_TO_ENROLL_SETTING_ERROR);
	    			}
	    		}
	    	}
	    }
	    
	    return responseWrapperService.wrapperCreate("S100", "예약설정 등록 성공");
	}
	
	private int checkMinutes(String time) {
	    String[] parts = time.split(":");
	    int minutes = Integer.parseInt(parts[1]);
	    if (minutes % 10 == 0) return 1;
	    else throw new TimeValueException(ErrorCode.TIME_VALUE_ERROR);
	}
	
	

	@Override
	public ObjectResponseWrapper<ReservationSettingRequestDTO> selectSettingByMemberNo() {
		CustomUserDetails memeber = authService.getUserDetails();
		String memberNo = memeber.getMemberNo();
		List<AvailableTimeDTO> AvailableTimeList = reservationSettingMapper.selectAvailableTime(memberNo);
		ReservationSettingDTO reservationSettingDTO = reservationSettingMapper.selectSettingInfo(memberNo);
		
		ReservationSettingRequestDTO reservationSettingRequestDTO = ReservationSettingRequestDTO.builder()
				.reservation(AvailableTimeList)
				.settingInfo(reservationSettingDTO)
				.build();
		
		log.info("AvailableTimeList : {} ",AvailableTimeList);
		log.info("reservationSettingRequestDTO : {} ",reservationSettingRequestDTO);
		
		return responseWrapperService.wrapperCreate("S101", "예약설정 조회 성공",reservationSettingRequestDTO);
	}
	
	@Override
	public ObjectResponseWrapper<ReservationSettingRequestDTO> selectSettingByRestaurantNo(String restaurantNo) {
		List<AvailableTimeDTO> AvailableTimeList = reservationSettingMapper.selectAvailableTimeByRestaurantNo(restaurantNo);
		ReservationSettingDTO reservationSettingDTO = reservationSettingMapper.selectSettingInfoByRestaurantNo(restaurantNo);
		
		ReservationSettingRequestDTO reservationSettingRequestDTO = ReservationSettingRequestDTO.builder()
				.reservation(AvailableTimeList)
				.settingInfo(reservationSettingDTO)
				.build();
		
		log.info("AvailableTimeList : {} ",AvailableTimeList);
		log.info("reservationSettingRequestDTO : {} ",reservationSettingRequestDTO);
		
		return responseWrapperService.wrapperCreate("S101", "예약설정 조회 성공",reservationSettingRequestDTO);
	}

	
	
	
	@Override
	public ObjectResponseWrapper<String> updateSetting(ReservationSettingRequestDTO request) {
		List<AvailableTimeDTO> availableTime = request.getReservation();      
	    ReservationSettingDTO  settingInfo = request.getSettingInfo();     
	
	    ReservationSettingVo reservationSettingVo = ReservationSettingVo.builder()
	    		.restaurantNo(settingInfo.getRestaurantNo())
	    		.interval(settingInfo.getInterval())
	    		.maxNum(settingInfo.getMaxNum())
	    		.minNum(settingInfo.getMinNum())
	    		.maxTeamNum(settingInfo.getMaxTeamNum())
	    		.description(settingInfo.getDescription())
	    		.build();
	    
	    int resul1t = reservationSettingMapper.updateSettingInfo(reservationSettingVo);
	    
	    
	    for(AvailableTimeDTO time : availableTime) {
	    	
	    	String reservationStartTime = time.getReservationStartTime();
	    	String reservationEndTime = time.getReservationEndTime();
	    	
	    	if(!reservationStartTime.isEmpty() && !reservationEndTime.isEmpty() && reservationStartTime != null && reservationEndTime != null) {
	    		checkMinutes(reservationStartTime);
	    		checkMinutes(reservationEndTime);
	    		
	    		
	    		AvailableTimeVo availableTimeVo = AvailableTimeVo.builder()
	    									.restaurantNo(time.getRestaurantNo())
	    									.weekDay(time.getWeekDay())
	    									.reservationStartTime(time.getReservationStartTime())
	    									.reservationEndTime(time.getReservationEndTime())
	    									.build();
	    		
	    		int result = reservationSettingMapper.updateAvailableTime(availableTimeVo);
	    	}
	    }
	    
	    return responseWrapperService.wrapperCreate("S102", "예약설정 수정 성공");
	}
	
	
	@Override
	public ObjectResponseWrapper<String> deleteSetting(String restaurantNo) {
		int result = reservationSettingMapper.deleteAvailableTime(restaurantNo);
		int result1 = reservationSettingMapper.deleteSettingInfo(restaurantNo);
		return responseWrapperService.wrapperCreate("S103", "예약설정 삭제 성공");
	}

	@Override
	public ObjectResponseWrapper<RestaurantDTO> selectMyRestaurant() {
		CustomUserDetails memeber = authService.getUserDetails();
		String memberNo = memeber.getMemberNo();
		
		RestaurantDTO myRestaurantInfo = reservationSettingMapper.selectMyRestaurant(memberNo);
		log.info("myRestaurantInfo :{}",myRestaurantInfo);
		return responseWrapperService.wrapperCreate("S101", "내 가게 조회 성공",myRestaurantInfo);
	}



}
