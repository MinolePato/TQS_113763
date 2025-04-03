package tqs.cars.car;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import tqs.cars.car.Service.CarManagerService;
import tqs.cars.car.data.CarRepository;
import tqs.cars.car.model.Car;

public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarManagerService carManagerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenSaveCar_thenReturnCar() {
        Car car = new Car("Toyota", "Corolla", "Sedan", "Petrol");
        when(carRepository.save(any(Car.class))).thenReturn(car);

        Car savedCar = carManagerService.save(car);

        assertEquals(car, savedCar);
        verify(carRepository, times(1)).save(car);
    }

    @Test
    public void givenCars_whenGetAllCars_thenReturnCarList() {
        Car car1 = new Car("Toyota", "Corolla", "Sedan", "Petrol");
        Car car2 = new Car("Honda", "Civic", "Sedan", "Petrol");
        List<Car> allCars = Arrays.asList(car1, car2);

        when(carRepository.findAll()).thenReturn(allCars);

        List<Car> cars = carManagerService.getAllCars();

        assertEquals(allCars, cars);
        verify(carRepository, times(1)).findAll();
    }

    @Test
    public void givenCarId_whenGetCarDetails_thenReturnCar() {
        Car car = new Car("Toyota", "Corolla", "Sedan", "Petrol");
        when(carRepository.findByCarId(anyLong())).thenReturn(car);

        Optional<Car> foundCar = carManagerService.getCarDetails(1L);

        assertEquals(Optional.of(car), foundCar);
        verify(carRepository, times(1)).findByCarId(1L);
    }



    @Test
    public void givenCar_whenFindSuitableReplacement_thenReturnMatchingCar() {
        Car car = new Car("Toyota", "Corolla", "Sedan", "Petrol");
        Car replacementCar = new Car("Honda", "Civic", "Sedan", "Petrol");
        List<Car> allCars = Arrays.asList(replacementCar);

        when(carRepository.findAll()).thenReturn(allCars);

        Optional<Car> foundCar = carManagerService.findSuitableReplacement(car);

        assertEquals(Optional.of(replacementCar), foundCar);
        verify(carRepository, times(1)).findAll();
    }
}
