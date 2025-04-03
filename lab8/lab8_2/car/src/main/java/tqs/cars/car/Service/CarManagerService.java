package tqs.cars.car.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import tqs.cars.car.model.Car;
import tqs.cars.car.data.CarRepository;

import java.util.Optional;

@Service
public class CarManagerService {

    final CarRepository carRepository;

    public CarManagerService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car save(Car oneCar) {
        return carRepository.save(oneCar);
    }

    public List<Car> getAllCars() {

        return carRepository.findAll();
    }

    public Optional<Car> getCarDetails(Long carId) {
        return Optional.of(carRepository.findByCarId(carId));
    }

    public Optional<Car> findSuitableReplacement(Car car) {
        List<Car> allCars = carRepository.findAll();
        return allCars.stream()
                .filter(c -> c.getSegment().equals(car.getSegment()) && c.getMotorType().equals(car.getMotorType()))
                .findFirst();
    }
}
