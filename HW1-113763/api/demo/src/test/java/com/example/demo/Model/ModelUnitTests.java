package com.example.demo.Model;

import org.junit.jupiter.api.Test;

import com.example.demo.models.Meal;
import com.example.demo.models.Reservation;
import com.example.demo.models.ReservationStatus;
import com.example.demo.models.Restaurant;
import com.example.demo.models.WeatherForecastDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelUnitTests {

    @Test
    public void testMealModel() {
        // Create a restaurant for association
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("Test Location");

        // Test Meal setters and getters
        Meal meal = new Meal();
        meal.setId(1L);
        meal.setName("Test Meal");
        meal.setDescrição("Test Description");
        meal.setPrice(15.99);
        LocalDate today = LocalDate.now();
        meal.setDate(today);
        LocalTime noon = LocalTime.NOON;
        meal.setTime(noon);
        meal.setNumberOfMeals(10);
        meal.setRestaurant(restaurant);

        // Verify properties
        assertThat(meal.getId()).isEqualTo(1L);
        assertThat(meal.getName()).isEqualTo("Test Meal");
        assertThat(meal.getDescrição()).isEqualTo("Test Description");
        assertThat(meal.getPrice()).isEqualTo(15.99);
        assertThat(meal.getDate()).isEqualTo(today);
        assertThat(meal.getTime()).isEqualTo(noon);
        assertThat(meal.getNumberOfMeals()).isEqualTo(10);
        assertThat(meal.getRestaurant()).isEqualTo(restaurant);
    }

    @Test
    public void testReservationModel() {
        // Create a restaurant and meal for association
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("Test Location");

        Meal meal = new Meal();
        meal.setId(1L);
        meal.setName("Test Meal");
        meal.setRestaurant(restaurant);

        // Test Reservation
        Reservation reservation = new Reservation();
        String reservationCode = reservation.getReservationCode(); // Code generated in constructor
        
        reservation.setId(1L);
        reservation.setCustomerName("John Doe");
        reservation.setRestaurant(restaurant);
        reservation.setMeal(meal);
        reservation.setStatus(ReservationStatus.VERIFIED); // Change from default REGISTERED

        // Verify properties
        assertThat(reservation.getId()).isEqualTo(1L);
        assertThat(reservation.getReservationCode()).isEqualTo(reservationCode);
        assertThat(reservation.getCustomerName()).isEqualTo("John Doe");
        assertThat(reservation.getRestaurant()).isEqualTo(restaurant);
        assertThat(reservation.getMeal()).isEqualTo(meal);
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.VERIFIED);
    }

    @Test
    public void testRestaurantModel() {
        // Test Restaurant setters and getters
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("Test Location");

        // Verify properties
        assertThat(restaurant.getId()).isEqualTo(1L);
        assertThat(restaurant.getName()).isEqualTo("Test Restaurant");
        assertThat(restaurant.getLocation()).isEqualTo("Test Location");
    }

    @Test
    public void testReservationCodeGeneration() {
        // Test that multiple reservation codes are unique
        Reservation reservation1 = new Reservation();
        String code1 = reservation1.getReservationCode();
        
        Reservation reservation2 = new Reservation();
        String code2 = reservation2.getReservationCode();
        
        Reservation reservation3 = new Reservation();
        String code3 = reservation3.getReservationCode();

        // Verify codes are not null and unique
        assertThat(code1).isNotNull();
        assertThat(code2).isNotNull();
        assertThat(code3).isNotNull();
        
        assertThat(code1).isNotEqualTo(code2);
        assertThat(code1).isNotEqualTo(code3);
        assertThat(code2).isNotEqualTo(code3);
        
        // Verify code format (8 alphanumeric characters)
        assertThat(code1).matches("[A-Z0-9]{8}");
        assertThat(code2).matches("[A-Z0-9]{8}");
        assertThat(code3).matches("[A-Z0-9]{8}");
    }

    @Test
    public void testReservationStatusEnum() {
        // Test enum values
        ReservationStatus registeredStatus = ReservationStatus.REGISTERED;
        ReservationStatus verifiedStatus = ReservationStatus.VERIFIED;
        ReservationStatus canceledStatus = ReservationStatus.CANCELED;

        // Verify enum values
        assertThat(registeredStatus.name()).isEqualTo("REGISTERED");
        assertThat(verifiedStatus.name()).isEqualTo("VERIFIED");
        assertThat(canceledStatus.name()).isEqualTo("CANCELED");
        
        // Test ordinals
        assertThat(registeredStatus.ordinal()).isEqualTo(0);
        assertThat(verifiedStatus.ordinal()).isEqualTo(1);
        assertThat(canceledStatus.ordinal()).isEqualTo(2);
    }
    @Test
    public void testAllArgsConstructorAndGetters() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 4, 10, 14, 0);
        double temperature = 15.5;
        String weatherDescription = "Partly Cloudy";
        int cloudPercentage = 60;
        double windSpeed = 5.2;
        int timestamp = 1234567890;

        WeatherForecastDTO forecast = new WeatherForecastDTO(
                dateTime, temperature, weatherDescription, cloudPercentage, windSpeed, timestamp
        );

        assertEquals(dateTime, forecast.getDateTime());
        assertEquals(15.5, forecast.getTemperature());
        assertEquals("Partly Cloudy", forecast.getWeatherDescription());
        assertEquals(60, forecast.getCloudPercentage());
        assertEquals(5.2, forecast.getWindSpeed());
        assertEquals(1234567890, forecast.getTimestamp());
    }

    @Test
    public void testSetters() {
        WeatherForecastDTO forecast = new WeatherForecastDTO();

        LocalDateTime dateTime = LocalDateTime.now();
        forecast.setDateTime(dateTime);
        forecast.setTemperature(20.0);
        forecast.setWeatherDescription("Sunny");
        forecast.setCloudPercentage(10);
        forecast.setWindSpeed(3.5);
        forecast.setTimestamp(987654321);

        assertEquals(dateTime, forecast.getDateTime());
        assertEquals(20.0, forecast.getTemperature());
        assertEquals("Sunny", forecast.getWeatherDescription());
        assertEquals(10, forecast.getCloudPercentage());
        assertEquals(3.5, forecast.getWindSpeed());
        assertEquals(987654321, forecast.getTimestamp());
    }
}