package com.example.demo.services;

import com.example.demo.models.Meal;
import com.example.demo.models.Reservation;
import com.example.demo.models.ReservationStatus;
import com.example.demo.models.Restaurant;
import com.example.demo.repositories.MealRepository;
import com.example.demo.repositories.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Restaurant restaurant;
    private Meal meal;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        // Set up test data
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("Test Location");

        meal = new Meal();
        meal.setId(1L);
        meal.setName("Test Meal");
        meal.setDescrição("Test Description");
        meal.setPrice(15.99);
        meal.setDate(LocalDate.now());
        meal.setTime(LocalTime.NOON);
        meal.setNumberOfMeals(10);
        meal.setRestaurant(restaurant);

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setReservationCode("ABCD1234");
        reservation.setCustomerName("John Doe");
        reservation.setStatus(ReservationStatus.REGISTERED);
        reservation.setRestaurant(restaurant);
        reservation.setMeal(meal);
    }

    @Test
    void createReservation_WithValidData_ShouldCreateReservation() {
        // Given
        Meal mealWithAvailability = new Meal();
        mealWithAvailability.setId(1L);
        mealWithAvailability.setNumberOfMeals(5);
        mealWithAvailability.setName("Test Meal");
        mealWithAvailability.setRestaurant(restaurant);

        Reservation newReservation = new Reservation();
        newReservation.setCustomerName("Jane Smith");
        newReservation.setRestaurant(restaurant);
        Meal mealReference = new Meal();
        mealReference.setId(1L);
        newReservation.setMeal(mealReference);

        Reservation savedReservation = new Reservation();
        savedReservation.setId(2L);
        savedReservation.setReservationCode("EFGH5678");
        savedReservation.setCustomerName("Jane Smith");
        savedReservation.setStatus(ReservationStatus.REGISTERED);
        savedReservation.setRestaurant(restaurant);
        savedReservation.setMeal(mealWithAvailability);

        when(mealRepository.findById(1L)).thenReturn(Optional.of(mealWithAvailability));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);

        // When
        Reservation result = reservationService.createReservation(newReservation);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getReservationCode()).isEqualTo("EFGH5678");
        assertThat(result.getStatus()).isEqualTo(ReservationStatus.REGISTERED);
        
        // Verify meal count was decremented
        verify(mealRepository, times(1)).findById(1L);
        verify(mealRepository, times(1)).save(argThat(m -> m.getNumberOfMeals() == 4));
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void createReservation_WithNoMealsAvailable_ShouldThrowException() {
        // Given
        Meal mealWithNoAvailability = new Meal();
        mealWithNoAvailability.setId(1L);
        mealWithNoAvailability.setNumberOfMeals(0); // No meals available
        mealWithNoAvailability.setName("Test Meal");
        mealWithNoAvailability.setRestaurant(restaurant);

        Reservation newReservation = new Reservation();
        newReservation.setCustomerName("Jane Smith");
        newReservation.setRestaurant(restaurant);
        Meal mealReference = new Meal();
        mealReference.setId(1L);
        newReservation.setMeal(mealReference);

        when(mealRepository.findById(1L)).thenReturn(Optional.of(mealWithNoAvailability));

        // When/Then
        assertThatThrownBy(() -> reservationService.createReservation(newReservation))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Não há mais refeições disponíveis");
        
        verify(mealRepository, times(1)).findById(1L);
        verify(mealRepository, never()).save(any(Meal.class));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void createReservation_WithInvalidMeal_ShouldThrowException() {
        // Given
        Reservation newReservation = new Reservation();
        newReservation.setCustomerName("Jane Smith");
        newReservation.setRestaurant(restaurant);
        newReservation.setMeal(null); // Invalid meal

        // When/Then
        assertThatThrownBy(() -> reservationService.createReservation(newReservation))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Reserva deve ter uma refeição válida");
        
        verify(mealRepository, never()).findById(any());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void getReservationByCode_WithExistingCode_ShouldReturnReservation() {
        // Given
        when(reservationRepository.findByReservationCode("ABCD1234")).thenReturn(Optional.of(reservation));

        // When
        Reservation result = reservationService.getReservationByCode("ABCD1234");

        // Then
        assertThat(result).isEqualTo(reservation);
        verify(reservationRepository, times(1)).findByReservationCode("ABCD1234");
    }

    @Test
    void getReservationByCode_WithNonExistingCode_ShouldThrowException() {
        // Given
        when(reservationRepository.findByReservationCode("NONEXIST")).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> reservationService.getReservationByCode("NONEXIST"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Reserva não encontrada");
        
        verify(reservationRepository, times(1)).findByReservationCode("NONEXIST");
    }

    @Test
    void cancelReservation_WithRegisteredReservation_ShouldCancelAndIncreaseMealCount() {
        // Given
        Reservation reservationToCancel = new Reservation();
        reservationToCancel.setId(1L);
        reservationToCancel.setReservationCode("ABCD1234");
        reservationToCancel.setStatus(ReservationStatus.REGISTERED);
        
        Meal associatedMeal = new Meal();
        associatedMeal.setId(1L);
        associatedMeal.setNumberOfMeals(5);
        reservationToCancel.setMeal(associatedMeal);

        when(reservationRepository.findByReservationCode("ABCD1234")).thenReturn(Optional.of(reservationToCancel));

        // When
        reservationService.cancelReservation("ABCD1234");

        // Then
        verify(reservationRepository, times(1)).findByReservationCode("ABCD1234");
        verify(mealRepository, times(1)).save(argThat(m -> m.getNumberOfMeals() == 6));
        verify(reservationRepository, times(1)).save(argThat(r -> r.getStatus() == ReservationStatus.CANCELED));
    }

    @Test
    void cancelReservation_WithAlreadyCanceledReservation_ShouldNotIncreaseMealCount() {
        // Given
        Reservation alreadyCanceledReservation = new Reservation();
        alreadyCanceledReservation.setId(1L);
        alreadyCanceledReservation.setReservationCode("ABCD1234");
        alreadyCanceledReservation.setStatus(ReservationStatus.CANCELED);
        
        Meal associatedMeal = new Meal();
        associatedMeal.setId(1L);
        associatedMeal.setNumberOfMeals(5);
        alreadyCanceledReservation.setMeal(associatedMeal);

        when(reservationRepository.findByReservationCode("ABCD1234")).thenReturn(Optional.of(alreadyCanceledReservation));

        // When
        reservationService.cancelReservation("ABCD1234");

        // Then
        verify(reservationRepository, times(1)).findByReservationCode("ABCD1234");
        verify(mealRepository, never()).save(any(Meal.class));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void checkInReservation_WithRegisteredReservation_ShouldVerifyAndReturnTrue() {
        // Given
        Reservation registeredReservation = new Reservation();
        registeredReservation.setId(1L);
        registeredReservation.setReservationCode("ABCD1234");
        registeredReservation.setStatus(ReservationStatus.REGISTERED);

        when(reservationRepository.findByReservationCode("ABCD1234")).thenReturn(Optional.of(registeredReservation));

        // When
        boolean result = reservationService.checkInReservation("ABCD1234");

        // Then
        assertThat(result).isTrue();
        verify(reservationRepository, times(1)).findByReservationCode("ABCD1234");
        verify(reservationRepository, times(1)).save(argThat(r -> r.getStatus() == ReservationStatus.VERIFIED));
    }

    @Test
    void checkInReservation_WithAlreadyVerifiedReservation_ShouldReturnFalse() {
        // Given
        Reservation verifiedReservation = new Reservation();
        verifiedReservation.setId(1L);
        verifiedReservation.setReservationCode("ABCD1234");
        verifiedReservation.setStatus(ReservationStatus.VERIFIED);

        when(reservationRepository.findByReservationCode("ABCD1234")).thenReturn(Optional.of(verifiedReservation));

        // When
        boolean result = reservationService.checkInReservation("ABCD1234");

        // Then
        assertThat(result).isFalse();
        verify(reservationRepository, times(1)).findByReservationCode("ABCD1234");
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void checkInReservation_WithCanceledReservation_ShouldReturnFalse() {
        // Given
        Reservation canceledReservation = new Reservation();
        canceledReservation.setId(1L);
        canceledReservation.setReservationCode("ABCD1234");
        canceledReservation.setStatus(ReservationStatus.CANCELED);

        when(reservationRepository.findByReservationCode("ABCD1234")).thenReturn(Optional.of(canceledReservation));

        // When
        boolean result = reservationService.checkInReservation("ABCD1234");

        // Then
        assertThat(result).isFalse();
        verify(reservationRepository, times(1)).findByReservationCode("ABCD1234");
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void updateReservation_WithMealChange_ShouldUpdateMealCounts() {
        // Given
        // Current reservation with meal1
        Reservation existingReservation = new Reservation();
        existingReservation.setId(1L);
        existingReservation.setReservationCode("ABCD1234");
        existingReservation.setStatus(ReservationStatus.REGISTERED);
        existingReservation.setCustomerName("John Doe");
        
        Meal oldMeal = new Meal();
        oldMeal.setId(1L);
        oldMeal.setNumberOfMeals(5);
        existingReservation.setMeal(oldMeal);
        
        // New meal to change to
        Meal newMeal = new Meal();
        newMeal.setId(2L);
        newMeal.setNumberOfMeals(10);
        
        // Update request with new meal
        Reservation updateRequest = new Reservation();
        Meal mealReference = new Meal();
        mealReference.setId(2L);
        updateRequest.setMeal(mealReference);

        when(reservationRepository.findByReservationCode("ABCD1234")).thenReturn(Optional.of(existingReservation));
        when(mealRepository.findById(2L)).thenReturn(Optional.of(newMeal));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(existingReservation);

        // When
        Reservation result = reservationService.updateReservation("ABCD1234", updateRequest);

        // Then
        verify(reservationRepository, times(1)).findByReservationCode("ABCD1234");
        verify(mealRepository, times(1)).save(argThat(m -> m.getId().equals(1L) && m.getNumberOfMeals() == 6));
        verify(mealRepository, times(1)).save(argThat(m -> m.getId().equals(2L) && m.getNumberOfMeals() == 9));
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void updateReservation_WithCanceledReservation_ShouldThrowException() {
        // Given
        Reservation canceledReservation = new Reservation();
        canceledReservation.setId(1L);
        canceledReservation.setReservationCode("ABCD1234");
        canceledReservation.setStatus(ReservationStatus.CANCELED);
        
        Reservation updateRequest = new Reservation();
        updateRequest.setCustomerName("New Name");

        when(reservationRepository.findByReservationCode("ABCD1234")).thenReturn(Optional.of(canceledReservation));

        // When/Then
        assertThatThrownBy(() -> reservationService.updateReservation("ABCD1234", updateRequest))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Cannot update canceled reservation");
        
        verify(reservationRepository, times(1)).findByReservationCode("ABCD1234");
        verify(mealRepository, never()).save(any(Meal.class));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void getAllReservations_ShouldReturnAllReservations() {
        // Given
        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setReservationCode("CODE1");
        
        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setReservationCode("CODE2");
        
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation1, reservation2));

        // When
        List<Reservation> result = reservationService.getAllReservations();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(reservation1, reservation2);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void getReservationsByRestaurant_ShouldReturnRestaurantReservations() {
        // Given
        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setReservationCode("CODE1");
        reservation1.setRestaurant(restaurant);
        
        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setReservationCode("CODE2");
        reservation2.setRestaurant(restaurant);
        
        when(reservationRepository.findByRestaurantId(1L)).thenReturn(Arrays.asList(reservation1, reservation2));

        // When
        List<Reservation> result = reservationService.getReservationsByRestaurant(1L);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(reservation1, reservation2);
        verify(reservationRepository, times(1)).findByRestaurantId(1L);
    }
}