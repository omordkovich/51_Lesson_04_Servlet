package app.repository;

import app.model.Car;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarRepositoryMap implements ICarRepository {

    private Map<Long, Car> database = new HashMap<>();
    private long currentId; //0

    public CarRepositoryMap() {
        initData();
    }

    private void initData() {
        save(new Car("VW", new BigDecimal(15000),2015));
        save(new Car("Mazda", new BigDecimal(30000),2022));
        save(new Car("Ford", new BigDecimal(40000),2025));
    }


    @Override
    public List<Car> getAll() {
        return database.values().stream().toList();
    }

    @Override
    public Car save(Car car) {
       car.setId(++currentId);
       database.put(car.getId(), car);
       return car;
    }
}
