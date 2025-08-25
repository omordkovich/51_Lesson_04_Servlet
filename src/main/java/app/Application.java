package app;

import app.model.Car;
import app.repository.CarRepositoryDB;

import java.math.BigDecimal;
import java.util.List;

public class Application {
    public static void main(String[] args) {

        CarRepositoryDB repo = new CarRepositoryDB();
        repo.createData();

        List<Car> cars = repo.getAll();

        for (Car car : cars) {
            System.out.println(car);
        }
        System.out.println("==================");
        repo.save(new Car("BMW", new BigDecimal(56666), 2025));
        repo.delete(2);
        repo.update(1, new BigDecimal(5232));
        repo.update(new Car(3L, "Ford", new BigDecimal(66666), 2025));

        cars = repo.getAll();

        for (Car car : cars) {
            System.out.println(car);
        }
    }

}
