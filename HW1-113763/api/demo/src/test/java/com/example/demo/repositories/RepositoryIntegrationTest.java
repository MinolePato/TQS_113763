package com.example.demo.repositories;

import com.example.demo.models.*;
import com.example.demo.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void testCompleteReservationFlow() {
        // Create a restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Flow Test Restaurant");
        restaurant.setLocation("Test Location");
        restaurant = restaurantRepository.save(restaurant);

        // Create a meal for the restaurant
        Meal meal = new Meal();
        meal.setName("Flow Test Meal");
        meal.setDescrição("Meal for testing the complete flow");
        meal.setPrice(25.99);
        meal.setDate(LocalDate.now());
        meal.setTime(LocalTime.of(19, 0));
        meal.setNumberOfMeals(20);
        meal.setRestaurant(restaurant);
        meal = mealRepository.save(meal);

        // Create a reservation
        Reservation reservation = new Reservation();
        reservation.setCustomerName("Flow Test Customer");
        reservation.setRestaurant(restaurant);
        reservation.setMeal(meal);
        reservation = reservationRepository.save(reservation);

        // Verify the reservation was created with correct associations
        assertThat(reservation.getId()).isNotNull();
        assertThat(reservation.getReservationCode()).isNotEmpty();
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.REGISTERED);
        assertThat(reservation.getRestaurant().getId()).isEqualTo(restaurant.getId());
        assertThat(reservation.getMeal().getId()).isEqualTo(meal.getId());

        // Test finding the reservation by code
        Reservation foundByCode = reservationRepository.findByReservationCode(reservation.getReservationCode()).orElse(null);
        assertThat(foundByCode).isNotNull();
        assertThat(foundByCode.getId()).isEqualTo(reservation.getId());

        // Test finding reservations by restaurant
        List<Reservation> restaurantReservations = reservationRepository.findByRestaurantId(restaurant.getId());
        assertThat(restaurantReservations).hasSize(1);
        assertThat(restaurantReservations.get(0).getId()).isEqualTo(reservation.getId());

        // Test finding meals by restaurant
        List<Meal> restaurantMeals = mealRepository.findByRestaurantId(restaurant.getId());
        assertThat(restaurantMeals).hasSize(1);
        assertThat(restaurantMeals.get(0).getId()).isEqualTo(meal.getId());

        // Update the reservation status
        reservation.setStatus(ReservationStatus.VERIFIED);
        reservation = reservationRepository.save(reservation);
        
        // Verify the status was updated
        Reservation updatedReservation = reservationRepository.findById(reservation.getId()).get();
        assertThat(updatedReservation.getStatus()).isEqualTo(ReservationStatus.VERIFIED);

        // Cancel the reservation
        reservation.setStatus(ReservationStatus.CANCELED);
        reservation = reservationRepository.save(reservation);
        
        // Verify the cancellation
        Reservation canceledReservation = reservationRepository.findById(reservation.getId()).get();
        assertThat(canceledReservation.getStatus()).isEqualTo(ReservationStatus.CANCELED);
    }

    @Test
    public void testMultipleReservationsForSameMeal() {
        // Create a restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Popular Restaurant");
        restaurant.setLocation("Downtown");
        restaurant = restaurantRepository.save(restaurant);

        // Create a meal with limited availability
        Meal meal = new Meal();
        meal.setName("Popular Meal");
        meal.setDescrição("A very popular meal with limited seats");
        meal.setPrice(30.99);
        meal.setDate(LocalDate.now().plusDays(1));
        meal.setTime(LocalTime.of(20, 0));
        meal.setNumberOfMeals(5); // Only 5 meals available
        meal.setRestaurant(restaurant);
        meal = mealRepository.save(meal);

        // Create 3 reservations for this meal
        for (int i = 1; i <= 3; i++) {
            Reservation reservation = new Reservation();
            reservation.setCustomerName("Customer " + i);
            reservation.setRestaurant(restaurant);
            reservation.setMeal(meal);
            reservationRepository.save(reservation);
        }

        // Verify we have 3 reservations for this meal
        List<Reservation> restaurantReservations = reservationRepository.findByRestaurantId(restaurant.getId());
        assertThat(restaurantReservations).hasSize(3);
        
        // In a real application, you might have logic to check if meal.getNumberOfMeals() > number of reservations
        // before allowing a new reservation. This would be part of a service layer test.
    }

    @Test
    public void testRestaurantWithMultipleMeals() {
        // Create a restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Varied Menu Restaurant");
        restaurant.setLocation("City Center");
        restaurant = restaurantRepository.save(restaurant);

        // Create multiple meals for the restaurant
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        
        Meal breakfast = new Meal();
        breakfast.setName("Breakfast Special");
        breakfast.setDescrição("Morning breakfast option");
        breakfast.setPrice(12.99);
        breakfast.setDate(tomorrow);
        breakfast.setTime(LocalTime.of(8, 0));
        breakfast.setNumberOfMeals(15);
        breakfast.setRestaurant(restaurant);
        mealRepository.save(breakfast);

        Meal lunch = new Meal();
        lunch.setName("Lunch Menu");
        lunch.setDescrição("Midday meal option");
        lunch.setPrice(18.99);
        lunch.setDate(tomorrow);
        lunch.setTime(LocalTime.of(13, 0));
        lunch.setNumberOfMeals(20);
        lunch.setRestaurant(restaurant);
        mealRepository.save(lunch);

        Meal dinner = new Meal();
        dinner.setName("Dinner Deluxe");
        dinner.setDescrição("Evening dining experience");
        dinner.setPrice(25.99);
        dinner.setDate(tomorrow);
        dinner.setTime(LocalTime.of(19, 0));
        dinner.setNumberOfMeals(25);
        dinner.setRestaurant(restaurant);
        mealRepository.save(dinner);

        // Verify the restaurant has 3 meals
        List<Meal> restaurantMeals = mealRepository.findByRestaurantId(restaurant.getId());
        assertThat(restaurantMeals).hasSize(3);
        assertThat(restaurantMeals).extracting(Meal::getName)
                                   .containsExactlyInAnyOrder("Breakfast Special", "Lunch Menu", "Dinner Deluxe");
    }
}