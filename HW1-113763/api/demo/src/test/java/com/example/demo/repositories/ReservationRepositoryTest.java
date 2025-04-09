package com.example.demo.repositories;

import com.example.demo.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ReservationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void shouldSaveReservation() {
        // Given
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("Test Location");
        entityManager.persist(restaurant);

        Meal meal = new Meal();
        meal.setName("Test Meal");
        meal.setDescrição("Test Description");
        meal.setPrice(15.99);
        meal.setDate(LocalDate.now());
        meal.setTime(LocalTime.NOON);
        meal.setNumberOfMeals(10);
        meal.setRestaurant(restaurant);
        entityManager.persist(meal);

        Reservation reservation = new Reservation();
        reservation.setCustomerName("John Doe");
        reservation.setRestaurant(restaurant);
        reservation.setMeal(meal);
        // The reservation code is generated in the constructor

        // When
        Reservation savedReservation = reservationRepository.save(reservation);

        // Then
        assertThat(savedReservation).isNotNull();
        assertThat(savedReservation.getId()).isNotNull();
        assertThat(savedReservation.getReservationCode()).isNotEmpty();
        assertThat(savedReservation.getStatus()).isEqualTo(ReservationStatus.REGISTERED);
        assertThat(savedReservation.getCustomerName()).isEqualTo("John Doe");
    }

    @Test
    public void shouldFindByReservationCode() {
        // Given
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("Test Location");
        entityManager.persist(restaurant);

        Meal meal = new Meal();
        meal.setName("Test Meal");
        meal.setDescrição("Test Description");
        meal.setPrice(15.99);
        meal.setDate(LocalDate.now());
        meal.setTime(LocalTime.NOON);
        meal.setNumberOfMeals(10);
        meal.setRestaurant(restaurant);
        entityManager.persist(meal);

        Reservation reservation = new Reservation();
        reservation.setCustomerName("John Doe");
        reservation.setRestaurant(restaurant);
        reservation.setMeal(meal);
        String reservationCode = reservation.getReservationCode();
        
        entityManager.persist(reservation);
        entityManager.flush();

        // When
        Optional<Reservation> foundReservation = reservationRepository.findByReservationCode(reservationCode);

        // Then
        assertThat(foundReservation).isPresent();
        assertThat(foundReservation.get().getCustomerName()).isEqualTo("John Doe");
        assertThat(foundReservation.get().getReservationCode()).isEqualTo(reservationCode);
    }

    @Test
    public void shouldFindByRestaurantId() {
        // Given
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("Restaurant 1");
        restaurant1.setLocation("Location 1");
        entityManager.persist(restaurant1);

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setName("Restaurant 2");
        restaurant2.setLocation("Location 2");
        entityManager.persist(restaurant2);

        Meal meal1 = new Meal();
        meal1.setName("Meal 1");
        meal1.setDescrição("Description 1");
        meal1.setPrice(10.99);
        meal1.setDate(LocalDate.now());
        meal1.setTime(LocalTime.NOON);
        meal1.setNumberOfMeals(5);
        meal1.setRestaurant(restaurant1);
        entityManager.persist(meal1);

        Meal meal2 = new Meal();
        meal2.setName("Meal 2");
        meal2.setDescrição("Description 2");
        meal2.setPrice(12.99);
        meal2.setDate(LocalDate.now());
        meal2.setTime(LocalTime.of(18, 0));
        meal2.setNumberOfMeals(8);
        meal2.setRestaurant(restaurant2);
        entityManager.persist(meal2);

        Reservation reservation1 = new Reservation();
        reservation1.setCustomerName("Customer 1");
        reservation1.setRestaurant(restaurant1);
        reservation1.setMeal(meal1);
        entityManager.persist(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setCustomerName("Customer 2");
        reservation2.setRestaurant(restaurant1);
        reservation2.setMeal(meal1);
        entityManager.persist(reservation2);

        Reservation reservation3 = new Reservation();
        reservation3.setCustomerName("Customer 3");
        reservation3.setRestaurant(restaurant2);
        reservation3.setMeal(meal2);
        entityManager.persist(reservation3);

        entityManager.flush();

        // When
        List<Reservation> reservations = reservationRepository.findByRestaurantId(restaurant1.getId());

        // Then
        assertThat(reservations).hasSize(2);
        assertThat(reservations).extracting(Reservation::getCustomerName).containsExactlyInAnyOrder("Customer 1", "Customer 2");
    }

    @Test
    public void shouldUpdateReservationStatus() {
        // Given
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("Test Location");
        entityManager.persist(restaurant);

        Meal meal = new Meal();
        meal.setName("Test Meal");
        meal.setDescrição("Test Description");
        meal.setPrice(15.99);
        meal.setDate(LocalDate.now());
        meal.setTime(LocalTime.NOON);
        meal.setNumberOfMeals(10);
        meal.setRestaurant(restaurant);
        entityManager.persist(meal);

        Reservation reservation = new Reservation();
        reservation.setCustomerName("John Doe");
        reservation.setRestaurant(restaurant);
        reservation.setMeal(meal);
        entityManager.persist(reservation);
        entityManager.flush();

        // When
        Reservation savedReservation = reservationRepository.findById(reservation.getId()).get();
        savedReservation.setStatus(ReservationStatus.VERIFIED);
        reservationRepository.save(savedReservation);

        // Then
        Reservation updatedReservation = reservationRepository.findById(reservation.getId()).get();
        assertThat(updatedReservation.getStatus()).isEqualTo(ReservationStatus.VERIFIED);
    }
}