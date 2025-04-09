package com.example.demo.controller;

import com.example.demo.controllers.WeatherController;
import com.example.demo.models.Meal;
import com.example.demo.models.Restaurant;
import com.example.demo.services.MealService;
import com.example.demo.services.WeatherService;
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
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @Mock
    private MealService mealService;

    @InjectMocks
    private WeatherController weatherController;

    private MockMvc mockMvc;
    private Meal meal;
    private Map<String, Object> weatherForecast;
    private Map<String, Object> cacheStats;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("London");

        meal = new Meal();
        meal.setId(1L);
        meal.setName("Test Meal");
        meal.setRestaurant(restaurant);
        meal.setDate(LocalDate.now());
        meal.setTime(LocalTime.of(12, 0));

        // Setup weather forecast data
        Map<String, Object> main = new HashMap<>();
        main.put("temp", 20.5);
        main.put("humidity", 65);

        Map<String, Object> weather = new HashMap<>();
        weather.put("description", "Clear sky");
        weather.put("icon", "01d");

        weatherForecast = new HashMap<>();
        weatherForecast.put("dt", 1728572400);
        weatherForecast.put("main", main);
        weatherForecast.put("weather", weather);

        // Setup cache statistics
        cacheStats = new HashMap<>();
        cacheStats.put("totalRequests", 100L);
        cacheStats.put("cacheHits", 80L);
        cacheStats.put("cacheMisses", 20L);
        cacheStats.put("hitRate", 0.8);
        cacheStats.put("cacheSize", 50);
        cacheStats.put("cacheTtlSeconds", 300.0);
    }

    @Test
    void getWeatherForMeal_WhenMealExists_ShouldReturnWeatherForecast() throws Exception {
        // Given
        when(mealService.getMealById(1L)).thenReturn(meal);
        when(weatherService.getWeatherForecastForMeal(meal)).thenReturn(weatherForecast);

        // When/Then
        mockMvc.perform(get("/Api/meals/1/weather")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dt", is(1728572400)));

        verify(mealService, times(1)).getMealById(1L);
        verify(weatherService, times(1)).getWeatherForecastForMeal(meal);
    }

    @Test
    void getWeatherForMeal_WhenMealDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(mealService.getMealById(99L)).thenReturn(null);

        // When/Then
        mockMvc.perform(get("/Api/meals/99/weather")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(mealService, times(1)).getMealById(99L);
        verify(weatherService, never()).getWeatherForecastForMeal(any(Meal.class));
    }

    @Test
    void getCacheStatistics_ShouldReturnCacheStats() throws Exception {
        // Given
        when(weatherService.getCacheStatistics()).thenReturn(cacheStats);

        // When/Then
        mockMvc.perform(get("/Api/weather/cache/stats")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRequests", is(100)))
                .andExpect(jsonPath("$.cacheHits", is(80)))
                .andExpect(jsonPath("$.cacheMisses", is(20)))
                .andExpect(jsonPath("$.hitRate", is(0.8)))
                .andExpect(jsonPath("$.cacheSize", is(50)))
                .andExpect(jsonPath("$.cacheTtlSeconds", is(300.0)));

        verify(weatherService, times(1)).getCacheStatistics();
    }

    @Test
    void clearCache_ShouldClearCacheAndReturnSuccess() throws Exception {
        // Given
        doNothing().when(weatherService).clearCache();

        // When/Then
        mockMvc.perform(post("/Api/weather/cache/clear")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(weatherService, times(1)).clearCache();
    }
}