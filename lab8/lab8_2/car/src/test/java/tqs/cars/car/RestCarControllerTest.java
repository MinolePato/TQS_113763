package tqs.cars.car;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tqs.cars.car.Controller.CarController;
import tqs.cars.car.Service.CarManagerService;
import tqs.cars.car.model.Car;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
public class RestCarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarManagerService carManagerService;

    @AfterEach
    public void resetDb() {
        reset(carManagerService);
    }

    @Test
    public void whenPostCar_thenCreateCar() throws Exception {
        Car car = new Car("Toyota", "Corolla");
        when(carManagerService.save(any())).thenReturn(car);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cars")
                .contentType("application/json")
                .content("{\"maker\": \"Toyota\", \"model\": \"Corolla\"}"))
                .andExpect(status().isCreated());

        verify(carManagerService, times(1)).save(any(Car.class));
    }

    @Test
    public void givenCars_whenGetCars_thenReturnJsonArray() throws Exception {
        Car car1 = new Car("Toyota", "Corolla");
        Car car2 = new Car("Honda", "Civic");
        List<Car> allCars = Arrays.asList(car1, car2);

        when(carManagerService.getAllCars()).thenReturn(allCars);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cars")
                .contentType("application/json"))
                .andExpect(status().isOk());

        verify(carManagerService, times(1)).getAllCars();
    }

    @Test
    public void givenCarId_whenGetCarById_thenReturnCar() throws Exception {
        Car car = new Car("Toyota", "Corolla");
        when(carManagerService.getCarDetails(anyLong())).thenReturn(Optional.of(car));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/1")
                .contentType("application/json"))
                .andExpect(status().isOk());

        verify(carManagerService, times(1)).getCarDetails(anyLong());
    }

    @Test
    public void givenInvalidCarId_whenGetCarById_thenReturnNotFound() throws Exception {
        when(carManagerService.getCarDetails(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/999")
                .contentType("application/json"))
                .andExpect(status().isNotFound());

        verify(carManagerService, times(1)).getCarDetails(anyLong());
    }
}
