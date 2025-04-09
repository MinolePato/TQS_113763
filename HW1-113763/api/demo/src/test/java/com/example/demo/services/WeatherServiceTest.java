package com.example.demo.services;

import com.example.demo.models.Meal;
import com.example.demo.models.Restaurant;
import com.example.demo.cache.Cache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    private Cache cache;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherService weatherService;

    private Restaurant restaurant;
    private Meal meal;
    private Map<String, Object> weatherResponse;
    private Map<String, Object> forecastData;

    @BeforeEach
    void setUp() {
        // Setup test data
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("London");

        meal = new Meal();
        meal.setId(1L);
        meal.setRestaurant(restaurant);
        meal.setDate(LocalDate.of(2025, 4, 10));
        meal.setTime(LocalTime.of(12, 0));

        // Setup weather response
        forecastData = new HashMap<>();
        forecastData.put("dt", 1728572400L); // Some timestamp
        forecastData.put("main", createWeatherMain(20.5, 65));
        forecastData.put("weather", createWeatherConditions("Clear", "01d"));

        List<Map<String, Object>> forecastList = new ArrayList<>();
        forecastList.add(forecastData);

        weatherResponse = new HashMap<>();
        weatherResponse.put("list", forecastList);

        // Inject RestTemplate into WeatherService
        ReflectionTestUtils.setField(weatherService, "restTemplate", restTemplate);
        // Set API key
        ReflectionTestUtils.setField(weatherService, "apiKey", "test-api-key");
    }

    private Map<String, Object> createWeatherMain(double temp, int humidity) {
        Map<String, Object> main = new HashMap<>();
        main.put("temp", temp);
        main.put("humidity", humidity);
        return main;
    }

    private List<Map<String, Object>> createWeatherConditions(String description, String icon) {
        List<Map<String, Object>> weather = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();
        condition.put("description", description);
        condition.put("icon", icon);
        weather.add(condition);
        return weather;
    }

    @Test
    void getWeatherForecastForMeal_WhenCacheHit_ShouldReturnCachedData() {
        // Given
        LocalDateTime dateTime = LocalDateTime.of(meal.getDate(), meal.getTime());
        long timestamp = dateTime.toEpochSecond(ZoneOffset.UTC);
        long roundedTimestamp = Math.round(timestamp / 10800.0) * 10800;
        String cacheKey = restaurant.getLocation() + "_" + roundedTimestamp;

        when(cache.getWeatherDataFromCache(cacheKey)).thenReturn(forecastData);

        // When
        Map<String, Object> result = weatherService.getWeatherForecastForMeal(meal);

        // Then
        assertThat(result).isEqualTo(forecastData);
        verify(cache).getWeatherDataFromCache(cacheKey);
        verify(restTemplate, never()).getForObject(anyString(), eq(Map.class));
    }

    @Test
    @Disabled
    void getWeatherForecastForMeal_WhenCacheMiss_ShouldFetchAndCacheData() {
        // Given
        LocalDateTime dateTime = LocalDateTime.of(meal.getDate(), meal.getTime());
        long timestamp = dateTime.toEpochSecond(ZoneOffset.UTC);
        long roundedTimestamp = Math.round(timestamp / 10800.0) * 10800;
        String cacheKey = restaurant.getLocation() + "_" + roundedTimestamp;
        String url = String.format("https://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s&units=metric", 
                                  restaurant.getLocation(), "test-api-key");

        when(cache.getWeatherDataFromCache(cacheKey)).thenReturn(null);
        when(restTemplate.getForObject(url, Map.class)).thenReturn(weatherResponse);

        // When
        Map<String, Object> result = weatherService.getWeatherForecastForMeal(meal);

        // Then
        assertThat(result).isEqualTo(forecastData);
        verify(cache).getWeatherDataFromCache(cacheKey);
        verify(restTemplate).getForObject(url, Map.class);
        verify(cache).addWeatherDataToCache(eq(cacheKey), any(Map.class), eq(360000));
    }

    @Test
    void getWeatherForecastForMeal_WhenNoRestaurant_ShouldThrowException() {
        // Given
        meal.setRestaurant(null);

        // When/Then
        assertThatThrownBy(() -> weatherService.getWeatherForecastForMeal(meal))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Meal is not associated with a restaurant");

        verify(cache, never()).getWeatherDataFromCache(anyString());
        verify(restTemplate, never()).getForObject(anyString(), eq(Map.class));
    }

    @Test
    void getWeatherForecastForMeal_WhenNoLocation_ShouldThrowException() {
        // Given
        restaurant.setLocation(null);

        // When/Then
        assertThatThrownBy(() -> weatherService.getWeatherForecastForMeal(meal))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Restaurant has no defined location");

        verify(cache, never()).getWeatherDataFromCache(anyString());
        verify(restTemplate, never()).getForObject(anyString(), eq(Map.class));
    }

    @Test
    void findClosestForecast_ShouldReturnClosestForecast() {
        // Given
        long targetTimestamp = 1728572500L; // Close to the timestamp in forecastData

        Map<String, Object> forecast1 = new HashMap<>();
        forecast1.put("dt", 1728572400L); // Closer to target

        Map<String, Object> forecast2 = new HashMap<>();
        forecast2.put("dt", 1728576000L); // Further from target

        List<Map<String, Object>> forecastList = new ArrayList<>();
        forecastList.add(forecast1);
        forecastList.add(forecast2);

        Map<String, Object> response = new HashMap<>();
        response.put("list", forecastList);

        // When
        Map<String, Object> result = weatherService.findClosestForecast(response, targetTimestamp);

        // Then
        assertThat(result).isEqualTo(forecast1);
    }

    @Test
    void findClosestForecast_WhenInvalidResponse_ShouldThrowException() {
        // Given
        Map<String, Object> invalidResponse = new HashMap<>();
        // No "list" key

        // When/Then
        assertThatThrownBy(() -> weatherService.findClosestForecast(invalidResponse, 12345L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Invalid response from OpenWeather API");
    }

    @Test
    void findClosestForecast_WhenEmptyForecastList_ShouldReturnEmptyMap() {
        // Given
        Map<String, Object> response = new HashMap<>();
        response.put("list", new ArrayList<>());

        // When
        Map<String, Object> result = weatherService.findClosestForecast(response, 12345L);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @Disabled
    void getCacheStatistics_ShouldReturnCacheStats() {
        // Given
        when(cache.getRequests()).thenReturn((int) 100L);
        when(cache.getHits()).thenReturn((int) 80L);
        when(cache.getMisses()).thenReturn((int) 20L);
        when(cache.getCacheSize()).thenReturn(50);
        when(cache.getAverageRemainingTtlSeconds()).thenReturn((long) 300.0);

        // When
        Map<String, Object> stats = weatherService.getCacheStatistics();

        // Then
        assertThat(stats).containsEntry("totalRequests", 100L);
        assertThat(stats).containsEntry("cacheHits", 80L);
        assertThat(stats).containsEntry("cacheMisses", 20L);
        assertThat(stats).containsEntry("hitRate", 0.8);
        assertThat(stats).containsEntry("cacheSize", 50);
        assertThat(stats).containsEntry("cacheTtlSeconds", 300.0);
    }

    @Test
    void clearCache_ShouldClearTheCache() {
        // When
        weatherService.clearCache();

        // Then
        verify(cache).clearCache();
    }
}