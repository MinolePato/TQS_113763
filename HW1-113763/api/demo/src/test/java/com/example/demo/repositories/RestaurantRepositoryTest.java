package com.example.demo.repositories;

import com.example.demo.models.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RestaurantRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    public void shouldSaveRestaurant() {
        // Given
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("Test Location");

        // When
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        // Then
        assertThat(savedRestaurant).isNotNull();
        assertThat(savedRestaurant.getId()).isNotNull();
        assertThat(savedRestaurant.getName()).isEqualTo("Test Restaurant");
        assertThat(savedRestaurant.getLocation()).isEqualTo("Test Location");
    }

    @Test
    public void shouldFindRestaurantById() {
        // Given
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setLocation("Test Location");
        entityManager.persist(restaurant);
        entityManager.flush();

        // When
        Optional<Restaurant> foundRestaurant = restaurantRepository.findById(restaurant.getId());

        // Then
        assertThat(foundRestaurant).isPresent();
        assertThat(foundRestaurant.get().getName()).isEqualTo("Test Restaurant");
        assertThat(foundRestaurant.get().getLocation()).isEqualTo("Test Location");
    }

    @Test
    public void shouldFindAllRestaurants() {
        // Given
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("Restaurant 1");
        restaurant1.setLocation("Location 1");
        entityManager.persist(restaurant1);

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setName("Restaurant 2");
        restaurant2.setLocation("Location 2");
        entityManager.persist(restaurant2);

        Restaurant restaurant3 = new Restaurant();
        restaurant3.setName("Restaurant 3");
        restaurant3.setLocation("Location 3");
        entityManager.persist(restaurant3);

        entityManager.flush();

        // When
        List<Restaurant> restaurants = restaurantRepository.findAll();

        // Then
        assertThat(restaurants).hasSize(3);
        assertThat(restaurants).extracting(Restaurant::getName).containsExactlyInAnyOrder("Restaurant 1", "Restaurant 2", "Restaurant 3");
    }

    @Test
    public void shouldUpdateRestaurant() {
        // Given
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Initial Name");
        restaurant.setLocation("Initial Location");
        entityManager.persist(restaurant);
        entityManager.flush();

        // When
        Restaurant savedRestaurant = restaurantRepository.findById(restaurant.getId()).get();
        savedRestaurant.setName("Updated Name");
        savedRestaurant.setLocation("Updated Location");
        restaurantRepository.save(savedRestaurant);

        // Then
        Restaurant updatedRestaurant = restaurantRepository.findById(restaurant.getId()).get();
        assertThat(updatedRestaurant.getName()).isEqualTo("Updated Name");
        assertThat(updatedRestaurant.getLocation()).isEqualTo("Updated Location");
    }

    @Test
    public void shouldDeleteRestaurant() {
        // Given
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Restaurant to Delete");
        restaurant.setLocation("Location");
        entityManager.persist(restaurant);
        entityManager.flush();

        // When
        restaurantRepository.deleteById(restaurant.getId());

        // Then
        Optional<Restaurant> deletedRestaurant = restaurantRepository.findById(restaurant.getId());
        assertThat(deletedRestaurant).isEmpty();
    }
}