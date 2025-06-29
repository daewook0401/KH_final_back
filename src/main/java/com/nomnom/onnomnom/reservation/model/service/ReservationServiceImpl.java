package com.nomnom.onnomnom.reservation.model.service;

import org.springframework.stereotype.Service;

import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.reservation.model.dto.ReservationDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {@Override
	public ObjectResponseWrapper<String> insertReservation(ReservationDTO reservationDTO) {
		return null;
	}

	@Override
	public ObjectResponseWrapper<String> selectReservation(ReservationDTO reservationDTO) {
		return null;
	}

	@Override
	public ObjectResponseWrapper<String> deleteReservation(ReservationDTO reservationDTO) {
		return null;
	}

}
