package com.example.demo.controller;

import com.example.demo.controllers.RestaurantController;
import com.example.demo.models.Meal;
import com.example.demo.models.Restaurant;
import com.example.demo.models.Reservation;
import com.example.demo.services.MealService;
import com.example.demo.services.RestaurantService;
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

import java.time.LocalDate;
import java.time.LocalTime;
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
public class RestaurantControllerTest {

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private MealService mealService;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private RestaurantController restaurantController;

    private MockMvc mockMvc;
    private Restaurant restaurant1;
    private Restaurant restaurant2;
    private Meal meal1;
    private Reservation reservation1;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();

        restaurant1 = new Restaurant();
        restaurant1.setId(1L);
        restaurant1.setName("Restaurant 1");
        restaurant1.setLocation("Location 1");

        restaurant2 = new Restaurant();
        restaurant2.setId(2L);
        restaurant2.setName("Restaurant 2");
        restaurant2.setLocation("Location 2");

        meal1 = new Meal();
        meal1.setId(1L);
        meal1.setName("Meal 1");
        meal1.setRestaurant(restaurant1);

        reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setReservationCode("RES123");
        reservation1.setRestaurant(restaurant1);
    }

    @Test
    void getAllRestaurants_ShouldReturnAllRestaurants() throws Exception {
        // Given
        List<Restaurant> restaurants = Arrays.asList(restaurant1, restaurant2);
        when(restaurantService.getAllRestaurants()).thenReturn(restaurants);

        // When/Then
        mockMvc.perform(get("/Api/restaurants")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Restaurant 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Restaurant 2")));

        verify(restaurantService, times(1)).getAllRestaurants();
    }

    @Test
    void createRestaurant_ShouldCreateAndReturnRestaurant() throws Exception {
        // Given
        when(restaurantService.saveRestaurant(any(Restaurant.class))).thenReturn(restaurant1);

        // When/Then
        mockMvc.perform(post("/Api/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Restaurant 1\",\"location\":\"Location 1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Restaurant 1")))
                .andExpect(jsonPath("$.location", is("Location 1")));

        verify(restaurantService, times(1)).saveRestaurant(any(Restaurant.class));
    }

    @Test
    void getRestaurantById_WhenRestaurantExists_ShouldReturnRestaurant() throws Exception {
        // Given
        when(restaurantService.getRestaurantById(1L)).thenReturn(restaurant1);

        // When/Then
        mockMvc.perform(get("/Api/restaurants/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Restaurant 1")));

        verify(restaurantService, times(1)).getRestaurantById(1L);
    }

    @Test
    void updateRestaurant_WhenRestaurantExists_ShouldUpdateAndReturnRestaurant() throws Exception {
        // Given
        when(restaurantService.updateRestaurant(eq(1L), any(Restaurant.class))).thenReturn(restaurant1);

        // When/Then
        mockMvc.perform(put("/Api/restaurants/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Restaurant\",\"location\":\"Updated Location\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Restaurant 1")));

        verify(restaurantService, times(1)).updateRestaurant(eq(1L), any(Restaurant.class));
    }

    @Test
    void getMenusByRestaurant_ShouldReturnMenusForRestaurant() throws Exception {
        // Given
        List<Meal> meals = Arrays.asList(meal1);
        when(mealService.getMenusByRestaurant(1L)).thenReturn(meals);

        // When/Then
        mockMvc.perform(get("/Api/restaurants/1/menus")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Meal 1")));

        verify(mealService, times(1)).getMenusByRestaurant(1L);
    }

    @Test
    void getReservationsByRestaurant_ShouldReturnReservationsForRestaurant() throws Exception {
        // Given
        List<Reservation> reservations = Arrays.asList(reservation1);
        when(reservationService.getReservationsByRestaurant(1L)).thenReturn(reservations);

        // When/Then
        mockMvc.perform(get("/Api/restaurants/1/reservations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].reservationCode", is("RES123")));

        verify(reservationService, times(1)).getReservationsByRestaurant(1L);
    }
}