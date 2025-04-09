package com.example.demo.services;

import com.example.demo.models.Meal;
import com.example.demo.models.Restaurant;
import com.example.demo.repositories.MealRepository;
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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private MealService mealService;

    private Restaurant restaurant;
    private Meal meal1;
    private Meal meal2;

    @BeforeEach
    void setUp() {
        // Set up test data
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("Test Location");

        meal1 = new Meal();
        meal1.setId(1L);
        meal1.setName("Meal 1");
        meal1.setDescrição("Description 1");
        meal1.setPrice(10.99);
        meal1.setDate(LocalDate.now());
        meal1.setTime(LocalTime.NOON);
        meal1.setNumberOfMeals(5);
        meal1.setRestaurant(restaurant);

        meal2 = new Meal();
        meal2.setId(2L);
        meal2.setName("Meal 2");
        meal2.setDescrição("Description 2");
        meal2.setPrice(15.99);
        meal2.setDate(LocalDate.now());
        meal2.setTime(LocalTime.of(18, 0));
        meal2.setNumberOfMeals(3);
        meal2.setRestaurant(restaurant);
    }

    @Test
    void getAllMeals_ShouldReturnAllMeals() {
        // Given
        when(mealRepository.findAll()).thenReturn(Arrays.asList(meal1, meal2));

        // When
        List<Meal> result = mealService.getAllMeals();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(meal1, meal2);
        verify(mealRepository, times(1)).findAll();
    }

    @Test
    void saveMeal_WithNullNumberOfMeals_ShouldInitializeToZero() {
        // Given
        Meal mealToSave = new Meal();
        mealToSave.setName("New Meal");
        mealToSave.setDescrição("New Description");
        mealToSave.setPrice(20.99);
        mealToSave.setDate(LocalDate.now());
        mealToSave.setTime(LocalTime.of(20, 0));
        mealToSave.setRestaurant(restaurant);
        mealToSave.setNumberOfMeals(null); 

        Meal savedMeal = new Meal();
        savedMeal.setId(3L);
        savedMeal.setName("New Meal");
        savedMeal.setDescrição("New Description");
        savedMeal.setPrice(20.99);
        savedMeal.setDate(LocalDate.now());
        savedMeal.setTime(LocalTime.of(20, 0));
        savedMeal.setRestaurant(restaurant);
        savedMeal.setNumberOfMeals(0); 

        when(mealRepository.save(any(Meal.class))).thenReturn(savedMeal);

        // When
        Meal result = mealService.saveMeal(mealToSave);

        // Then
        assertThat(result.getNumberOfMeals()).isEqualTo(0);
        verify(mealRepository, times(1)).save(argThat(meal -> meal.getNumberOfMeals() == 0));
    }

    @Test
    void saveMeal_WithSetNumberOfMeals_ShouldNotChangeValue() {
        // Given
        Meal mealToSave = new Meal();
        mealToSave.setName("New Meal");
        mealToSave.setPrice(20.99);
        mealToSave.setRestaurant(restaurant);
        mealToSave.setNumberOfMeals(10); 

        Meal savedMeal = new Meal();
        savedMeal.setId(3L);
        savedMeal.setName("New Meal");
        savedMeal.setPrice(20.99);
        savedMeal.setRestaurant(restaurant);
        savedMeal.setNumberOfMeals(10); 

        when(mealRepository.save(any(Meal.class))).thenReturn(savedMeal);

        // When
        Meal result = mealService.saveMeal(mealToSave);

        // Then
        assertThat(result.getNumberOfMeals()).isEqualTo(10);
        verify(mealRepository, times(1)).save(argThat(meal -> meal.getNumberOfMeals() == 10));
    }

    @Test
    void getMealById_WithExistingId_ShouldReturnMeal() {

        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal1));


        Meal result = mealService.getMealById(1L);


        assertThat(result).isEqualTo(meal1);
        verify(mealRepository, times(1)).findById(1L);
    }

    @Test
    void getMealById_WithNonExistingId_ShouldThrowException() {

        when(mealRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mealService.getMealById(999L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Refeição não encontrada");
        
        verify(mealRepository, times(1)).findById(999L);
    }

    @Test
    void updateMeal_WithExistingId_ShouldUpdateAndReturnMeal() {
 
        Meal updatedMeal = new Meal();
        updatedMeal.setName("Updated Name");
        updatedMeal.setPrice(25.99);
        updatedMeal.setNumberOfMeals(8);

        Meal existingMeal = new Meal();
        existingMeal.setId(1L);
        existingMeal.setName("Original Name");
        existingMeal.setPrice(20.99);
        existingMeal.setNumberOfMeals(5);
        existingMeal.setDescrição("Description");
        existingMeal.setDate(LocalDate.now());
        existingMeal.setTime(LocalTime.NOON);
        existingMeal.setRestaurant(restaurant);

        Meal resultMeal = new Meal();
        resultMeal.setId(1L);
        resultMeal.setName("Updated Name");
        resultMeal.setPrice(25.99);
        resultMeal.setNumberOfMeals(8);
        resultMeal.setDescrição("Description"); 
        resultMeal.setDate(LocalDate.now());
        resultMeal.setTime(LocalTime.NOON); 
        resultMeal.setRestaurant(restaurant); 

        when(mealRepository.findById(1L)).thenReturn(Optional.of(existingMeal));
        when(mealRepository.save(any(Meal.class))).thenReturn(resultMeal);

        // When
        Meal result = mealService.updateMeal(1L, updatedMeal);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Updated Name");
        assertThat(result.getPrice()).isEqualTo(25.99);
        assertThat(result.getNumberOfMeals()).isEqualTo(8);
        verify(mealRepository, times(1)).findById(1L);
        verify(mealRepository, times(1)).save(any(Meal.class));
    }

    @Test
    void updateMeal_WithNullNumberOfMeals_ShouldNotChangeValue() {
        // Given
        Meal updatedMeal = new Meal();
        updatedMeal.setName("Updated Name");
        updatedMeal.setPrice(25.99);
        updatedMeal.setNumberOfMeals(null); 

        Meal existingMeal = new Meal();
        existingMeal.setId(1L);
        existingMeal.setName("Original Name");
        existingMeal.setPrice(20.99);
        existingMeal.setNumberOfMeals(5); 
        existingMeal.setDescrição("Description");
        existingMeal.setRestaurant(restaurant);

        Meal resultMeal = new Meal();
        resultMeal.setId(1L);
        resultMeal.setName("Updated Name");
        resultMeal.setPrice(25.99);
        resultMeal.setNumberOfMeals(5); 
        resultMeal.setDescrição("Description");
        resultMeal.setRestaurant(restaurant);

        when(mealRepository.findById(1L)).thenReturn(Optional.of(existingMeal));
        when(mealRepository.save(any(Meal.class))).thenReturn(resultMeal);

        // When
        Meal result = mealService.updateMeal(1L, updatedMeal);

        // Then
        assertThat(result.getNumberOfMeals()).isEqualTo(5); 
        verify(mealRepository, times(1)).save(argThat(meal -> 
            meal.getName().equals("Updated Name") &&
            meal.getPrice() == 25.99 &&
            meal.getNumberOfMeals() == 5)); // Still 5
    }

    @Test
    void getMenusByRestaurant_ShouldReturnRestaurantMeals() {
        // Given
        when(mealRepository.findByRestaurantId(1L)).thenReturn(Arrays.asList(meal1, meal2));

        // When
        List<Meal> result = mealService.getMenusByRestaurant(1L);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(meal1, meal2);
        verify(mealRepository, times(1)).findByRestaurantId(1L);
    }
    @Test
void saveMeal_WithNegativePrice_ShouldThrowException() {
    // Given
    Meal mealToSave = new Meal();
    mealToSave.setName("Invalid Meal");
    mealToSave.setDescrição("This meal has a negative price");
    mealToSave.setPrice(-5.99); // preço inválido
    mealToSave.setNumberOfMeals(10);
    mealToSave.setRestaurant(restaurant);

    // Then
    assertThatThrownBy(() -> mealService.saveMeal(mealToSave))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Preço não pode ser negativo");

    verify(mealRepository, never()).save(any());
}

}