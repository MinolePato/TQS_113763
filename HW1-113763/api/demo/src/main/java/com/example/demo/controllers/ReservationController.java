package com.example.demo.controllers;

import com.example.demo.models.Reservation;
import com.example.demo.services.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("Api/reservations")
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        logger.info("Attempting to create a reservation: {}", reservation);
        Reservation createdReservation = reservationService.createReservation(reservation);
        logger.info("Reservation created successfully with ID: {}", createdReservation.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);  
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        logger.info("Fetching all reservations.");
        List<Reservation> reservations = reservationService.getAllReservations();
        logger.info("Fetched {} reservations.", reservations.size());
        return reservations;
    }

    @GetMapping("/{reservationCode}")
    public Reservation getReservationByCode(@PathVariable String reservationCode) {
        logger.info("Fetching reservation with code: {}", reservationCode);
        Reservation reservation = reservationService.getReservationByCode(reservationCode);
        logger.info("Found reservation: {}", reservation);
        return reservation;
    }

    @DeleteMapping("/{reservationCode}")
    public ResponseEntity<Map<String, String>> cancelReservation(@PathVariable String reservationCode) {
        logger.info("Attempting to cancel reservation with code: {}", reservationCode);
        Map<String, String> response = new HashMap<>();
        
        try {
            reservationService.cancelReservation(reservationCode);
            logger.info("Reservation with code {} has been successfully canceled.", reservationCode);
            
            response.put("status", "success");
            response.put("message", "Reservation successfully canceled");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error canceling reservation with code {}: {}", reservationCode, e.getMessage());
            
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/{reservationCode}/check-in")
    public ResponseEntity<Map<String, String>> checkIn(@PathVariable String reservationCode) {
        logger.info("Attempting to check in reservation with code: {}", reservationCode);
        Map<String, String> response = new HashMap<>();
        
        boolean success = reservationService.checkInReservation(reservationCode);
        
        if (success) {
            logger.info("Check-in successful for reservation with code: {}", reservationCode);
            response.put("status", "success");
            response.put("message", "Reserva verificada com sucesso.");
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Failed to check in reservation with code: {}", reservationCode);
            response.put("status", "error");
            response.put("message", "Falha ao realizar check-in. A reserva já foi verificada ou cancelada.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{reservationCode}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable String reservationCode, @RequestBody Reservation reservation) {
        logger.info("Attempting to update reservation with code: {}", reservationCode);
        try {
            Reservation updatedReservation = reservationService.updateReservation(reservationCode, reservation);
            logger.info("Reservation with code {} updated successfully: {}", reservationCode, updatedReservation);
            return ResponseEntity.ok(updatedReservation);
        } catch (IllegalStateException e) {
            logger.warn("Cannot update canceled reservation with code: {}", reservationCode);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}