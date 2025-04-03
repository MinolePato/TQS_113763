package tqs.cars.car;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import tqs.cars.car.Service.CarManagerService;
import tqs.cars.car.data.CarRepository;
import tqs.cars.car.model.Car;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class CarServiceIntegrationTest {

    @Autowired
    private CarManagerService carManagerService;

    @Autowired
    private CarRepository carRepository;

    @AfterEach
    public void resetDb() {
        carRepository.deleteAll();
    }

    @Test
    public void whenSaveCar_thenReturnCar() {
        Car car = new Car("Toyota", "Corolla", "Sedan", "Petrol");
        carManagerService.save(car);

        List<Car> found = carRepository.findAll();
        assertThat(found).extracting(Car::getMaker).containsOnly("Toyota");
    }

    @Test
    public void givenCars_whenGetAllCars_thenReturnCarList() {
        Car car1 = new Car("Toyota", "Corolla", "Sedan", "Petrol");
        Car car2 = new Car("Honda", "Civic", "Sedan", "Petrol");
        carManagerService.save(car1);
        carManagerService.save(car2);

        List<Car> allCars = carManagerService.getAllCars();
        assertThat(allCars).hasSize(2).extracting(Car::getMaker).contains(car1.getMaker(), car2.getMaker());
    }

    @Test
    public void givenCarId_whenGetCarDetails_thenReturnCar() {
        Car car = new Car("Toyota", "Corolla", "Sedan", "Petrol");
        carManagerService.save(car);

        Optional<Car> foundCar = carManagerService.getCarDetails(car.getCarId());
        assertThat(foundCar).isPresent().get().isEqualTo(car);
    }

    @Test
    public void givenInvalidCarId_whenGetCarDetails_thenReturnEmpty() {
        Optional<Car> foundCar = carManagerService.getCarDetails(999L);
        assertThat(foundCar).isEmpty();
    }

    @Test
    public void givenCar_whenFindSuitableReplacement_thenReturnMatchingCar() {
        Car car = new Car("Toyota", "Corolla", "Sedan", "Petrol");
        Car replacementCar = new Car("Honda", "Civic", "Sedan", "Petrol");
        carManagerService.save(replacementCar);

        Optional<Car> foundCar = carManagerService.findSuitableReplacement(car);
        assertThat(foundCar).isPresent().get().isEqualTo(replacementCar);
    }
}
