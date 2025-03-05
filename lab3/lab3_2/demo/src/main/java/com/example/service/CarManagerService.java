package main.java.com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.persistence.model.Car;
import com.example.persistence.repo.CarRepository;



@Service
public class CarManagerService {
    private CarRepository carRepository;

    public CarManagerService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car save(Car car) {
        return carRepository.save(car);
    }

    public List<Car> getAllCars() {
        return (List<Car>) carRepository.findAll();
    }

    public Optional<Car> getCarDetails(Long carId) {
        Car car = carRepository.findByCarId(carId);
        return Optional.ofNullable(car);
    }

    public Car getCarByMaker(String maker) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Car getCarById(long l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
