package com.nomnom.onnomnom.reservation.model.service;

import com.nomnom.onnomnom.global.response.ObjectResponseWrapper;
import com.nomnom.onnomnom.reservation.model.dto.ReservationDTO;

public interface ReservationService {

	ObjectResponseWrapper<String> insertReservation(ReservationDTO reservationDTO);

	ObjectResponseWrapper<String> selectReservation(ReservationDTO reservationDTO);

	ObjectResponseWrapper<String> deleteReservation(ReservationDTO reservationDTO);

}
