package com.example.demo.controller;

import com.example.demo.controllers.MealController;
import com.example.demo.models.Meal;
import com.example.demo.services.MealService;
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
public class MealControllerTest {

    @Mock
    private MealService mealService;

    @InjectMocks
    private MealController mealController;

    private MockMvc mockMvc;
    private Meal meal1;
    private Meal meal2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(mealController).build();

        meal1 = new Meal();
        meal1.setId(1L);
        meal1.setName("Meal 1");
        meal1.setDescrição("Description 1");
        meal1.setPrice(10.0);
        meal1.setDate(LocalDate.now());
        meal1.setTime(LocalTime.of(12, 0));

        meal2 = new Meal();
        meal2.setId(2L);
        meal2.setName("Meal 2");
        meal2.setDescrição("Description 2");
        meal2.setPrice(15.0);
        meal2.setDate(LocalDate.now());
        meal2.setTime(LocalTime.of(18, 0));
    }

    @Test
    void getAllMeals_ShouldReturnAllMeals() throws Exception {
        // Given
        List<Meal> meals = Arrays.asList(meal1, meal2);
        when(mealService.getAllMeals()).thenReturn(meals);

        // When/Then
        mockMvc.perform(get("/Api/meals")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Meal 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Meal 2")));

        verify(mealService, times(1)).getAllMeals();
    }

    @Test
    void createMeal_ShouldCreateAndReturnMeal() throws Exception {
        // Given
        when(mealService.saveMeal(any(Meal.class))).thenReturn(meal1);

        // When/Then
        mockMvc.perform(post("/Api/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Meal 1\",\"description\":\"Description 1\",\"price\":10.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Meal 1")));

        verify(mealService, times(1)).saveMeal(any(Meal.class));
    }

    @Test
    void getMealById_WhenMealExists_ShouldReturnMeal() throws Exception {
        // Given
        when(mealService.getMealById(1L)).thenReturn(meal1);

        // When/Then
        mockMvc.perform(get("/Api/meals/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Meal 1")));

        verify(mealService, times(1)).getMealById(1L);
    }

    @Test
    void updateMeal_WhenMealExists_ShouldUpdateAndReturnMeal() throws Exception {
        // Given
        when(mealService.updateMeal(eq(1L), any(Meal.class))).thenReturn(meal1);

        // When/Then
        mockMvc.perform(put("/Api/meals/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Meal\",\"description\":\"Updated Description\",\"price\":12.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Meal 1")));

        verify(mealService, times(1)).updateMeal(eq(1L), any(Meal.class));
    }
}