package app.repository;

import app.model.Car;

import java.math.BigDecimal;
import java.util.List;

public interface ICarRepository {
    List<Car> getAll();

    Car save(Car car);

    Car getById(long id);

    Car update(long id, BigDecimal price);

    Car update(Car car);

    void delete(long id);


}
