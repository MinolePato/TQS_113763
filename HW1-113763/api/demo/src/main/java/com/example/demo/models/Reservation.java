package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reservationCode = generateReservationCode() ;
    
    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.REGISTERED;

    @ManyToOne
    private Restaurant restaurant;

    @ManyToOne
    private Meal meal;

    private String customerName;
    public String generateReservationCode() {
        int length = 8;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
    
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            code.append(characters.charAt(index));
        }
    
        return code.toString();
    }

}
