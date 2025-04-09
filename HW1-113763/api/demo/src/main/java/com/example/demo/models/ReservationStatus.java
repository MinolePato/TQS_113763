package com.example.demo.models;

public enum ReservationStatus {
    REGISTERED,  // Initial state when reservation is created
    VERIFIED,    // When check-in is completed
    CANCELED     // When reservation is canceled
}
