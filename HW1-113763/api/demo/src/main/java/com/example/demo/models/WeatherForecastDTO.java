package com.example.demo.models;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherForecastDTO {
    private LocalDateTime dateTime;
    private double temperature;
    private String weatherDescription;
    private int cloudPercentage;
    private double windSpeed;
    private int timestamp;

   }
