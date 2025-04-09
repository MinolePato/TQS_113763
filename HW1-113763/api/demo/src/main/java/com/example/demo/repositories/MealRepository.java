package com.example.demo.repositories;
import com.example.demo.models.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;
@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
List<Meal> findByRestaurantId(Long restaurantId);
}