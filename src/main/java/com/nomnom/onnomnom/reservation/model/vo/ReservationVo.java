package com.nomnom.onnomnom.reservation.model.vo;

import java.sql.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReservationVo {

	private String reservationNo;
	private String restaurantNo;
	private String memberNo;
	private String reserveDay;
	private String reserveTime;
	private int numberOfGuests;
	private Date createDate;
	private String status;
}
