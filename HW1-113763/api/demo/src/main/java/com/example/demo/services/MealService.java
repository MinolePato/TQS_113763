package com.example.demo.services;

import com.example.demo.models.Meal;
import com.example.demo.repositories.MealRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealService {
    private static final Logger logger = LoggerFactory.getLogger(MealService.class);

    private final MealRepository mealRepository;

    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public List<Meal> getAllMeals() {
        logger.info("Fetching all meals.");
        return mealRepository.findAll();
    }

    public Meal saveMeal(Meal meal) {
        logger.info("Saving meal: {}", meal.getName());
        if (meal.getPrice() < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }
    
        if (meal.getNumberOfMeals() == null) {
            meal.setNumberOfMeals(0);
        }
        // Initialize numberOfMeals if not set
        if (meal.getNumberOfMeals() == null) {
            meal.setNumberOfMeals(0);
            logger.info("Initialized numberOfMeals to 0 for meal: {}", meal.getName());
        }
        
        return mealRepository.save(meal);
    }

    public Meal getMealById(Long mealId) {
        logger.info("Fetching meal with ID: {}", mealId);
        return mealRepository.findById(mealId)
                .orElseThrow(() -> {
                    logger.warn("Meal with ID {} not found", mealId);
                    return new IllegalArgumentException("Refeição não encontrada");
                });
    }

    public Meal updateMeal(Long mealId, Meal meal) {
        logger.info("Updating meal with ID: {}", mealId);
        Meal existingMeal = getMealById(mealId);
        existingMeal.setName(meal.getName());
        existingMeal.setPrice(meal.getPrice());
        
        // Update numberOfMeals if provided
        if (meal.getNumberOfMeals() != null) {
            existingMeal.setNumberOfMeals(meal.getNumberOfMeals());
            logger.info("Updated numberOfMeals to {} for meal: {}", 
                       meal.getNumberOfMeals(), existingMeal.getName());
        }
        
        return mealRepository.save(existingMeal);
    }

    public List<Meal> getMenusByRestaurant(Long restaurantId) {
        logger.info("Fetching meals for restaurant with ID: {}", restaurantId);
        return mealRepository.findByRestaurantId(restaurantId);
    }
}