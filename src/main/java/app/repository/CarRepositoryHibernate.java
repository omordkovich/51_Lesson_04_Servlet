package app.repository;

import app.model.Car;
import jakarta.persistence.EntityManager;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;
import java.util.List;

public class CarRepositoryHibernate implements ICarRepository {

    private EntityManager entityManager;

    public CarRepositoryHibernate() {
        entityManager = new Configuration()
                .configure("hibernate/postgres.cfg.xml")
                .buildSessionFactory()
                .createEntityManager();
    }

    @Override
    public List<Car> getAll() {
        return List.of();
    }

    @Override
    public Car save(Car car) {
        return null;
    }

    @Override
    public Car getById(long id) {
        return entityManager.find(Car.class, id);
    }

    @Override
    public Car update(long id, BigDecimal price) {
        Car car = entityManager.find(Car.class, id);
        car.setPrice(price);
        return entityManager.merge(car);
    }

    @Override
    public Car update(Car car) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
