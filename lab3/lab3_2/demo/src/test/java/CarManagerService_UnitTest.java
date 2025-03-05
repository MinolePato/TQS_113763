import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.persistence.model.Car;

import main.java.com.example.service.CarManagerService;

import com.example.persistence.repo.CarRepository;


@ExtendWith(MockitoExtension.class)
class CarService_UnitTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarManagerService carService;

    @BeforeEach
    void setUp() {
        Car toyota = new Car("Toyota", "Corolla");
        toyota.setCarid(101L);

        Car honda = new Car("Honda", "Civic");
        Car ford = new Car("Ford", "Focus");

        List<Car> allCars = Arrays.asList(toyota, honda, ford);

        Mockito.when(carRepository.findByMaker(toyota.getMaker())).thenReturn(toyota);
        Mockito.when(carRepository.findByMaker(honda.getMaker())).thenReturn(honda);
        Mockito.when(carRepository.findByMaker("unknown")).thenReturn(null);
        
        Mockito.when(carRepository.findAll()).thenReturn(allCars);
        Mockito.when(carRepository.findById(-1L)).thenReturn(Optional.empty());
    }

    @Test
    void whenSearchValidMaker_thenCarShouldBeFound() {
        String maker = "Honda";
        Car found = carService.getCarByMaker(maker);

        assertThat(found.getMaker()).isEqualTo(maker);
    }

    @Test
    void whenSearchInvalidMaker_thenCarShouldNotBeFound() {
        Car fromDb = carService.getCarByMaker("unknown");
        assertThat(fromDb).isNull();

        verifyFindByMakerIsCalledOnce("unknown");
    }

    @Test
    void whenValidId_thenCarShouldBeFound() {
        Car fromDb = carService.getCarById(101L);
        assertThat(fromDb.getMaker()).isEqualTo("Toyota");

        verifyFindByIdIsCalledOnce();
    }

    

    @Test
    void given3Cars_whenGetAll_thenReturn3Records() {
        List<Car> allCars = carService.getAllCars();
        verifyFindAllCarsIsCalledOnce();
        assertThat(allCars).hasSize(3).extracting(Car::getMaker).contains("Toyota", "Honda", "Ford");
    }

    private void verifyFindByMakerIsCalledOnce(String maker) {
        verify(carRepository, times(1)).findByMaker(maker);
    }

    private void verifyFindByIdIsCalledOnce() {
        verify(carRepository, times(1)).findById(anyLong());
    }

    private void verifyFindAllCarsIsCalledOnce() {
        verify(carRepository, times(1)).findAll();
    }
}