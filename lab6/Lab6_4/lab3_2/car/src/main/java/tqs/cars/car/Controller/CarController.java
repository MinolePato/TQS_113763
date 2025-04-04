package tqs.cars.car.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import tqs.cars.car.Service.CarManagerService;
import tqs.cars.car.model.Car;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CarController {

    private final CarManagerService carManagerService;

    public CarController(CarManagerService injectedCarManagerService) {
        this.carManagerService = injectedCarManagerService;
    }

    @PostMapping("/cars")
    public ResponseEntity<Car> createCar(@RequestBody Car oneCar) {
        HttpStatus status = HttpStatus.CREATED;
        Car saved = carManagerService.save(oneCar);
        return new ResponseEntity<>(saved, status);
    }

    @GetMapping(path = "/cars", produces = "application/json")
    public List<Car> getAllCars() {
        return carManagerService.getAllCars();
    }

    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable(value = "id") Long carId)
            throws ResourceAccessException {
        Optional<Car> car = carManagerService.getCarDetails(carId);
        if (car.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(car.get());
    }
}