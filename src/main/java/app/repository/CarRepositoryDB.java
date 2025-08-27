package app.repository;

import app.model.Car;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static app.constants.Constants.*;

public class CarRepositoryDB implements ICarRepository {

    //    docker run --name postgres51 -p 5433:5432 -e POSTGRES_USER=my_user -e POSTGRES_PASSWORD=pos1234 -e POSTGRES_DB=c_51_cars -d postgres;

    private Connection getConnection() {
        // jdbc:postgresql://10.2.3.5:5432/c_51_cars?user=my_user&password=pos1234
        try {
            Class.forName(DB_DRIVER_PATH);
            String dbUrl = String.format("%s%s?user=%s&password=%s", DB_ADDRESS, DB_NAME, DB_USER, DB_PASSWORD);
            return DriverManager.getConnection(dbUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> getAll() {
        List<Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Car car = new Car(
                        resultSet.getString("brand"),
                        resultSet.getBigDecimal("price"),
                        resultSet.getInt("year"));
                car.setId(resultSet.getLong("id"));
                cars.add(car);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return cars;
    }

    @Override
    public Car save(Car car) {
        try (Connection connection = getConnection()) {

            String query = String.format("INSERT INTO car (brand, price, year) VALUES ('%s', %s, %d);",
                    car.getBrand(), car.getPrice(), car.getYear());
            Statement statement = connection.createStatement();

            //execute() - Внесение изменения в базу данных
            //executeQuery() - для получения данных

            statement.execute(query);

            statement.execute(query, Statement.RETURN_GENERATED_KEYS);
            //Достать ID из Statement
            ResultSet resultSet = statement.getGeneratedKeys();

            resultSet.next(); //переходим на первую строку
            Long id = resultSet.getLong("id");

            car.setId(id);

            return car;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car getById(long id) {

        try (Connection connection = getConnection()) {
            String query = String.format("SELECT * FROM car WHERE id = %d;", id);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Пытаемся переключиться на первую колонку
            if (resultSet.next()) {
                String brand = resultSet.getString("brand");
                BigDecimal price = resultSet.getBigDecimal("price");
                int year = resultSet.getInt("year");
                return new Car(id, brand, price, year);
            }
            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car update(long id, BigDecimal price) {
        String query = String.format("UPDATE car SET price = %s WHERE id = %d;", price, id);
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(query);
            if (rows == 0) {
                System.out.println("No rows affected!");
                return null;
            }
            return getById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car update(Car car) {
        String query = String.format("UPDATE car SET price = %s WHERE id = %d;", car.getPrice(), car.getId());
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(query);
            if (rows == 0) {
                System.out.println("No car was found!");
                return null;
            }
            return car;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        String query = String.format("DELETE FROM car WHERE id = %d;", id);

        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(query);
            if (rows == 0) {
                System.out.println("No car was found!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void test(Car car) {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO car (brand, price, year) VALUES (?, ?, ?);";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, car.getBrand());
            ps.setBigDecimal(2, car.getPrice());
            ps.setInt(3, car.getYear());

            int rowsInserted = ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
