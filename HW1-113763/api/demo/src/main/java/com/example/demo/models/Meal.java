package com.example.demo.models;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String descrição;
    private double price;
    private LocalDate date;
    private LocalTime time;
    private Integer numberOfMeals; // New field for tracking available meal count
    
    @ManyToOne
    private Restaurant restaurant;
}