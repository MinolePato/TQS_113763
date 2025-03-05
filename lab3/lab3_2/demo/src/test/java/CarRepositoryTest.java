import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.persistence.model.Car;
import com.example.persistence.repo.CarRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// @TestPropertySource(locations = "application-integrationtest.properties") // Para testes com BD real
class CarRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    @AfterEach
    void tearDown() {
        carRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve encontrar um carro pelo ID")
    void whenFindByCarId_thenReturnCar() {
        Car car = new Car("Toyota", "Corolla");
        entityManager.persistAndFlush(car);

        Car found = carRepository.findByCarId(car.getId());
        assertThat(found).isNotNull();
        assertThat(found.getMaker()).isEqualTo(car.getMaker());
        assertThat(found.getModel()).isEqualTo(car.getModel());
    }

    @Test
    @DisplayName("Deve retornar null ao procurar um ID inexistente")
    void whenFindByInvalidCarId_thenReturnNull() {
        Car found = carRepository.findByCarId(-1L);
        assertThat(found).isNull();
    }

    @Test
    @DisplayName("Deve encontrar todos os carros")
    void whenFindAll_thenReturnAllCars() {
        Car car1 = new Car("Honda", "Civic");
        Car car2 = new Car("Ford", "Focus");

        entityManager.persist(car1);
        entityManager.persist(car2);
        entityManager.flush();

        List<Car> allCars = carRepository.findAll();
        assertThat(allCars).hasSize(2).extracting(Car::getMaker).contains("Honda", "Ford");
    }

    @Test
    @DisplayName("Deve encontrar carros por fabricante")
    void whenFindByMaker_thenReturnCars() {
        Car car1 = new Car("BMW", "X5");
        Car car2 = new Car("BMW", "320i");
        Car car3 = new Car("Audi", "A4");

        entityManager.persist(car1);
        entityManager.persist(car2);
        entityManager.persist(car3);
        entityManager.flush();

        List<Car> bmwCars = (List<Car>) carRepository.findByMaker("BMW");
        assertThat(bmwCars).hasSize(2).extracting(Car::getModel).contains("X5", "320i");
    }

}