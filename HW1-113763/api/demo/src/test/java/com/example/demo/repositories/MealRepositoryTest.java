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
public class MealRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MealRepository mealRepository;

    @Test
    public void shouldSaveMeal() {
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

        // When
        Meal savedMeal = mealRepository.save(meal);

        // Then
        assertThat(savedMeal).isNotNull();
        assertThat(savedMeal.getId()).isNotNull();
        assertThat(savedMeal.getName()).isEqualTo("Test Meal");
    }

    @Test
    public void shouldFindMealsByRestaurantId() {
        // Given
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("Test Location");
        entityManager.persist(restaurant);

        Meal meal1 = new Meal();
        meal1.setName("Meal 1");
        meal1.setDescrição("Description 1");
        meal1.setPrice(10.99);
        meal1.setDate(LocalDate.now());
        meal1.setTime(LocalTime.NOON);
        meal1.setNumberOfMeals(5);
        meal1.setRestaurant(restaurant);
        entityManager.persist(meal1);

        Meal meal2 = new Meal();
        meal2.setName("Meal 2");
        meal2.setDescrição("Description 2");
        meal2.setPrice(12.99);
        meal2.setDate(LocalDate.now());
        meal2.setTime(LocalTime.of(18, 0));
        meal2.setNumberOfMeals(8);
        meal2.setRestaurant(restaurant);
        entityManager.persist(meal2);

        // Create another restaurant with a meal
        Restaurant otherRestaurant = new Restaurant();
        otherRestaurant.setName("Other Restaurant");
        otherRestaurant.setLocation("Other Location");
        entityManager.persist(otherRestaurant);

        Meal meal3 = new Meal();
        meal3.setName("Meal 3");
        meal3.setDescrição("Description 3");
        meal3.setPrice(14.99);
        meal3.setDate(LocalDate.now());
        meal3.setTime(LocalTime.NOON);
        meal3.setNumberOfMeals(3);
        meal3.setRestaurant(otherRestaurant);
        entityManager.persist(meal3);

        entityManager.flush();

        // When
        List<Meal> meals = mealRepository.findByRestaurantId(restaurant.getId());

        // Then
        assertThat(meals).hasSize(2);
        assertThat(meals).extracting(Meal::getName).containsExactlyInAnyOrder("Meal 1", "Meal 2");
    }
}