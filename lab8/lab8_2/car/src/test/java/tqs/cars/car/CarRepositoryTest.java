package tqs.cars.car;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tqs.cars.car.data.CarRepository;
import tqs.cars.car.model.Car;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Test
    public void whenSaveCar_thenFindById() {
        Car car = new Car("Toyota", "Corolla");
        carRepository.save(car);

        Car foundCar = carRepository.findByCarId(car.getCarId());
        assertEquals(car, foundCar);
    }

    @Test
    public void whenFindAll_thenReturnCarList() {
        Car car1 = new Car("Toyota", "Corolla");
        Car car2 = new Car("Honda", "Civic");
        carRepository.save(car1);
        carRepository.save(car2);

        List<Car> allCars = carRepository.findAll();
        assertEquals(2, allCars.size());
        assertTrue(allCars.contains(car1));
        assertTrue(allCars.contains(car2));
    }
}