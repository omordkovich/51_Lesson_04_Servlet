package app.repository;

import app.model.Car;

import java.util.List;

public interface ICarRepository {
    List<Car> getAll();

    Car save(Car car);

    Car getById(long id);
}
