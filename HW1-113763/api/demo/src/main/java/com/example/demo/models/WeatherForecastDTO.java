package com.example.demo.models;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;

@Embeddable
public class WeatherForecastDTO {
    private LocalDateTime dateTime;
    private double temperature;
    private String weatherDescription;
    private int cloudPercentage;
    private double windSpeed;
    private int timestamp;

    public WeatherForecastDTO() {
    }

    public WeatherForecastDTO(LocalDateTime dateTime, double temperature, String weatherDescription,
            int cloudPercentage, double windSpeed, int timestamp) {
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.weatherDescription = weatherDescription;
        this.cloudPercentage = cloudPercentage;
        this.windSpeed = windSpeed;
        this.timestamp = timestamp;
    }}
