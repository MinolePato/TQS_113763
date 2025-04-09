package com.example.demo.controller;

import com.example.demo.controllers.ReservationController;
import com.example.demo.models.Reservation;
import com.example.demo.services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    private MockMvc mockMvc;
    private Reservation reservation1;
    private Reservation reservation2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();

        reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setReservationCode("RES123");
        reservation1.setCustomerName("John Doe");


        reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setReservationCode("RES456");
        reservation2.setCustomerName("Jane Smith");

    }

    @Test
    void createReservation_ShouldCreateAndReturnReservation() throws Exception {
        // Given
        when(reservationService.createReservation(any(Reservation.class))).thenReturn(reservation1);

        // When/Then
        mockMvc.perform(post("/Api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John Doe\",\"email\":\"john@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.reservationCode", is("RES123")))
                .andExpect(jsonPath("$.customerName", is("John Doe")));

        verify(reservationService, times(1)).createReservation(any(Reservation.class));
    }

    @Test
    void getAllReservations_ShouldReturnAllReservations() throws Exception {
        // Given
        List<Reservation> reservations = Arrays.asList(reservation1, reservation2);
        when(reservationService.getAllReservations()).thenReturn(reservations);

        // When/Then
        mockMvc.perform(get("/Api/reservations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].reservationCode", is("RES123")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].reservationCode", is("RES456")));

        verify(reservationService, times(1)).getAllReservations();
    }

    @Test
    void getReservationByCode_WhenReservationExists_ShouldReturnReservation() throws Exception {
        // Given
        when(reservationService.getReservationByCode("RES123")).thenReturn(reservation1);

        // When/Then
        mockMvc.perform(get("/Api/reservations/RES123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.reservationCode", is("RES123")))
                .andExpect(jsonPath("$.customerName", is("John Doe")));

        verify(reservationService, times(1)).getReservationByCode("RES123");
    }

    @Test
    void cancelReservation_WhenSuccessful_ShouldReturnSuccessMessage() throws Exception {
        // Given
        doNothing().when(reservationService).cancelReservation("RES123");

        // When/Then
        mockMvc.perform(delete("/Api/reservations/RES123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message", is("Reservation successfully canceled")));

        verify(reservationService, times(1)).cancelReservation("RES123");
    }

    @Test
    void cancelReservation_WhenFails_ShouldReturnErrorMessage() throws Exception {
        // Given
        doThrow(new RuntimeException("Reservation not found")).when(reservationService).cancelReservation("RES999");

        // When/Then
        mockMvc.perform(delete("/Api/reservations/RES999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Reservation not found")));

        verify(reservationService, times(1)).cancelReservation("RES999");
    }

    @Test
    void checkIn_WhenSuccessful_ShouldReturnSuccessMessage() throws Exception {
        // Given
        when(reservationService.checkInReservation("RES123")).thenReturn(true);

        // When/Then
        mockMvc.perform(post("/Api/reservations/RES123/check-in")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message", is("Reserva verificada com sucesso.")));

        verify(reservationService, times(1)).checkInReservation("RES123");
    }

    @Test
    void checkIn_WhenFails_ShouldReturnErrorMessage() throws Exception {
        // Given
        when(reservationService.checkInReservation("RES456")).thenReturn(false);

        // When/Then
        mockMvc.perform(post("/Api/reservations/RES456/check-in")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Falha ao realizar check-in. A reserva já foi verificada ou cancelada.")));

        verify(reservationService, times(1)).checkInReservation("RES456");
    }

    @Test
    void updateReservation_WhenSuccessful_ShouldReturnUpdatedReservation() throws Exception {
        // Given
        when(reservationService.updateReservation(eq("RES123"), any(Reservation.class))).thenReturn(reservation1);

        // When/Then
        mockMvc.perform(put("/Api/reservations/RES123")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Name\",\"email\":\"updated@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.reservationCode", is("RES123")));

        verify(reservationService, times(1)).updateReservation(eq("RES123"), any(Reservation.class));
    }

    @Test
    void updateReservation_WhenCanceled_ShouldReturnConflict() throws Exception {
        // Given
        when(reservationService.updateReservation(eq("RES456"), any(Reservation.class)))
                .thenThrow(new IllegalStateException("Cannot update canceled reservation"));

        // When/Then
        mockMvc.perform(put("/Api/reservations/RES456")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Name\",\"email\":\"updated@example.com\"}"))
                .andExpect(status().isConflict());

        verify(reservationService, times(1)).updateReservation(eq("RES456"), any(Reservation.class));
    }
}