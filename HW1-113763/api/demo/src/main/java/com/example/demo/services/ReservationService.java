package com.example.demo.services;

import com.example.demo.models.Reservation;
import com.example.demo.models.ReservationStatus;
import com.example.demo.models.Meal;
import com.example.demo.repositories.ReservationRepository;
import com.example.demo.repositories.MealRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;
    private final MealRepository mealRepository;

    public ReservationService(ReservationRepository reservationRepository, MealRepository mealRepository) {
        this.reservationRepository = reservationRepository;
        this.mealRepository = mealRepository;
    }

    @Transactional
    public Reservation createReservation(Reservation reservation) {
        logger.info("Creating reservation with code: {}", reservation.getReservationCode());
        
        // Get the meal and check if there are enough meals available
        Meal meal = reservation.getMeal();
        if (meal == null || meal.getId() == null) {
            logger.error("Reservation must have a valid meal");
            throw new IllegalArgumentException("Reserva deve ter uma refeição válida");
        }
        
        Meal mealEntity = mealRepository.findById(meal.getId())
            .orElseThrow(() -> {
                logger.warn("Meal with ID {} not found", meal.getId());
                return new IllegalArgumentException("Refeição não encontrada");
            });
        
        // Check if there are enough meals available
        if (mealEntity.getNumberOfMeals() <= 0) {
            logger.warn("No more meals available for meal with ID: {}", meal.getId());
            throw new IllegalStateException("Não há mais refeições disponíveis");
        }
        
        // Decrease the number of available meals
        mealEntity.setNumberOfMeals(mealEntity.getNumberOfMeals() - 1);
        mealRepository.save(mealEntity);
        logger.info("Decreased available meals for meal ID {}: now {}", mealEntity.getId(), mealEntity.getNumberOfMeals());
        
        // Ensure new reservations always have REGISTERED status
        reservation.setStatus(ReservationStatus.REGISTERED);
        reservation.setMeal(mealEntity); // Set the updated meal entity
        
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        logger.info("Fetching all reservations.");
        return reservationRepository.findAll();
    }

    public Reservation getReservationByCode(String reservationCode) {
        logger.info("Fetching reservation with code: {}", reservationCode);
        return reservationRepository.findByReservationCode(reservationCode)
                .orElseThrow(() -> {
                    logger.warn("Reservation with code {} not found", reservationCode);
                    return new IllegalArgumentException("Reserva não encontrada");
                });
    }

    @Transactional
    public void cancelReservation(String reservationCode) {
        logger.info("Cancelling reservation with code: {}", reservationCode);
        Reservation reservation = getReservationByCode(reservationCode);
        
        // Only increase meal count if the reservation wasn't already canceled
        if (reservation.getStatus() != ReservationStatus.CANCELED) {
            // Increase the number of available meals
            Meal meal = reservation.getMeal();
            if (meal != null && meal.getId() != null) {
                meal.setNumberOfMeals(meal.getNumberOfMeals() + 1);
                mealRepository.save(meal);
                logger.info("Increased available meals for meal ID {}: now {}", meal.getId(), meal.getNumberOfMeals());
            }
            
            // Update status to CANCELED
            reservation.setStatus(ReservationStatus.CANCELED);
            reservationRepository.save(reservation);
            logger.info("Reservation with code {} status changed to CANCELED", reservationCode);
        } else {
            logger.info("Reservation with code {} is already canceled", reservationCode);
        }
    }

    @Transactional
    public boolean checkInReservation(String reservationCode) {
        logger.info("Checking in reservation with code: {}", reservationCode);
        Reservation reservation = getReservationByCode(reservationCode);
        
        // Don't allow check-in for already verified or canceled reservations
        if (reservation.getStatus() == ReservationStatus.VERIFIED) {
            logger.warn("Reservation with code {} is already checked in", reservationCode);
            return false;
        }
        
        if (reservation.getStatus() == ReservationStatus.CANCELED) {
            logger.warn("Cannot check in canceled reservation with code {}", reservationCode);
            return false;
        }
        
        reservation.setStatus(ReservationStatus.VERIFIED);
        reservationRepository.save(reservation);
        logger.info("Reservation with code {} status changed to VERIFIED", reservationCode);
        return true;
    }

    @Transactional
    public Reservation updateReservation(String reservationCode, Reservation reservation) {
        logger.info("Updating reservation with code: {}", reservationCode);
        Reservation existingReservation = getReservationByCode(reservationCode);
        
        // Don't allow updates to CANCELED reservations
        if (existingReservation.getStatus() == ReservationStatus.CANCELED) {
            logger.warn("Cannot update canceled reservation with code {}", reservationCode);
            throw new IllegalStateException("Cannot update canceled reservation");
        }
        
        // Handle meal change - restore old meal count and reduce new meal count
        Meal oldMeal = existingReservation.getMeal();
        Meal newMeal = reservation.getMeal();
        
        if (newMeal != null && newMeal.getId() != null && 
            (oldMeal == null || !oldMeal.getId().equals(newMeal.getId()))) {
            
            // Return the old meal to inventory if there was one
            if (oldMeal != null) {
                oldMeal.setNumberOfMeals(oldMeal.getNumberOfMeals() + 1);
                mealRepository.save(oldMeal);
                logger.info("Increased available meals for old meal ID {}: now {}", 
                            oldMeal.getId(), oldMeal.getNumberOfMeals());
            }
            
            // Get and update the new meal
            Meal newMealEntity = mealRepository.findById(newMeal.getId())
                .orElseThrow(() -> {
                    logger.warn("New meal with ID {} not found", newMeal.getId());
                    return new IllegalArgumentException("Nova refeição não encontrada");
                });
                
            // Check if there are enough of the new meal
            if (newMealEntity.getNumberOfMeals() <= 0) {
                logger.warn("No more meals available for new meal with ID: {}", newMeal.getId());
                throw new IllegalStateException("Não há mais refeições disponíveis para a nova seleção");
            }
            
            // Reduce the count of the new meal
            newMealEntity.setNumberOfMeals(newMealEntity.getNumberOfMeals() - 1);
            mealRepository.save(newMealEntity);
            logger.info("Decreased available meals for new meal ID {}: now {}", 
                        newMealEntity.getId(), newMealEntity.getNumberOfMeals());
                        
            existingReservation.setMeal(newMealEntity);
        }
        
        // Don't overwrite the status with the input status - preserve workflow
        // existingReservation.setStatus(reservation.getStatus());
        
        return reservationRepository.save(existingReservation);
    }

    public List<Reservation> getReservationsByRestaurant(Long restaurantId) {
        logger.info("Fetching reservations for restaurant with ID: {}", restaurantId);
        return reservationRepository.findByRestaurantId(restaurantId);
    }
}