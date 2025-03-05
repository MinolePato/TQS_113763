/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.persistence.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.persistence.model.Car;


public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> fildAll();

    Car findByCarId(long carid);

    Car findByMaker(String maker);

    


}
