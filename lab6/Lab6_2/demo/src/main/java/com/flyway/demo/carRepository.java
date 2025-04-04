package com.flyway.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flyway.demo.model.Car;

public interface carRepository extends JpaRepository<Car, Long> {

}