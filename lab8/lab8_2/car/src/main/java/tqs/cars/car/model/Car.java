package tqs.cars.car.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    private String maker;
    private String model;
    private String segment;
    private String motorType;

    public Car() {
    }

    public Car(String maker, String model) {
        this.maker = maker;
        this.model = model;
    }

    public Car(String maker, String model, String segment, String motorType) {
        this.maker = maker;
        this.model = model;
        this.segment = segment;
        this.motorType = motorType;
    }

    public Car(Long carId, String maker, String model, String segment, String motorType) {
        this.carId = carId;
        this.maker = maker;
        this.model = model;
        this.segment = segment;
        this.motorType = motorType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Car car = (Car) o;
        return carId.equals(car.carId) && Objects.equals(maker, car.maker) && Objects.equals(model, car.model)
                && Objects.equals(segment, car.segment) && Objects.equals(motorType, car.motorType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carId, maker, model, segment, motorType);
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getMotorType() {
        return motorType;
    }

    public void setMotorType(String motorType) {
        this.motorType = motorType;
    }
}
