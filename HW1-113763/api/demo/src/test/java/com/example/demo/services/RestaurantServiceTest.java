package com.example.demo.services;

import com.example.demo.models.Restaurant;
import com.example.demo.repositories.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant restaurant1;
    private Restaurant restaurant2;

    @BeforeEach
    void setUp() {
        // Set up test data
        restaurant1 = new Restaurant();
        restaurant1.setId(1L);
        restaurant1.setName("Restaurant 1");
        restaurant1.setLocation("Location 1");

        restaurant2 = new Restaurant();
        restaurant2.setId(2L);
        restaurant2.setName("Restaurant 2");
        restaurant2.setLocation("Location 2");
    }

    @Test
    void getAllRestaurants_ShouldReturnAllRestaurants() {
        // Given
        when(restaurantRepository.findAll()).thenReturn(Arrays.asList(restaurant1, restaurant2));

        // When
        List<Restaurant> result = restaurantService.getAllRestaurants();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(restaurant1, restaurant2);
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    void saveRestaurant_ShouldSaveAndReturnRestaurant() {
        // Given
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setName("New Restaurant");
        newRestaurant.setLocation("New Location");
        
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(newRestaurant);
        
        // When
        Restaurant result = restaurantService.saveRestaurant(newRestaurant);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("New Restaurant");
        assertThat(result.getLocation()).isEqualTo("New Location");
        verify(restaurantRepository, times(1)).save(newRestaurant);
    }
    
    @Test
    void getRestaurantById_WhenRestaurantExists_ShouldReturnRestaurant() {
        // Given
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant1));
        
        // When
        Restaurant result = restaurantService.getRestaurantById(1L);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Restaurant 1");
        verify(restaurantRepository, times(1)).findById(1L);
    }
    
    @Test
    void getRestaurantById_WhenRestaurantDoesNotExist_ShouldThrowException() {
        // Given
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());
        
        // When/Then
        assertThatThrownBy(() -> restaurantService.getRestaurantById(99L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Restaurante não encontrado");
            
        verify(restaurantRepository, times(1)).findById(99L);
    }
    
    @Test
    void updateRestaurant_WhenRestaurantExists_ShouldUpdateAndReturnRestaurant() {
        // Given
        Restaurant updatedRestaurant = new Restaurant();
        updatedRestaurant.setName("Updated Restaurant");
        updatedRestaurant.setLocation("Updated Location");
        
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant1));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When
        Restaurant result = restaurantService.updateRestaurant(1L, updatedRestaurant);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Updated Restaurant");
        assertThat(result.getLocation()).isEqualTo("Updated Location");
        verify(restaurantRepository, times(1)).findById(1L);
        verify(restaurantRepository, times(1)).save(any(Restaurant.class));
    }
    
    @Test
    void updateRestaurant_WhenRestaurantDoesNotExist_ShouldThrowException() {
        // Given
        Restaurant updatedRestaurant = new Restaurant();
        updatedRestaurant.setName("Updated Restaurant");
        updatedRestaurant.setLocation("Updated Location");
        
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());
        
        // When/Then
        assertThatThrownBy(() -> restaurantService.updateRestaurant(99L, updatedRestaurant))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Restaurante não encontrado");
            
        verify(restaurantRepository, times(1)).findById(99L);
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }
}