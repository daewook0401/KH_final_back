package com.nomnom.onnomnom.reservation.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReservationDTO {

	private String reservationNo;
	private String restaurantNo;
	private String memberNo;
	private String reserveDay;
	private String reserveTime;
	private int numberOfGuests;
	private Date createDate;
	private String status;
	private int count;
}
