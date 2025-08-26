package app.repository;

import app.model.Car;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static app.constants.Constants.*;

public class CarRepositoryDB_Homework implements ICarRepository {
    private long currentId;

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

    private Connection conn = getConnection();


    public void initData() {
        save(new Car("VW", new BigDecimal(15000), 2015));
        save(new Car("Mazda", new BigDecimal(30000), 2022));
        save(new Car("Ford", new BigDecimal(40000), 2025));
    }


    @Override
    public List<Car> getAll() {
        List<Car> cars = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM cars");
            while (rs.next()) {
                Car car = new Car(rs.getString("brand"), rs.getBigDecimal("price"), rs.getInt("year"));
                car.setId(rs.getLong("id"));

                cars.add(car);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cars;
    }

    @Override
    public Car save(Car car) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO cars (brand, price, year) VALUES (?,?,?)");
            pstmt.setString(1, car.getBrand());
            pstmt.setBigDecimal(2, car.getPrice());
            pstmt.setInt(3, car.getYear());

            car.setId(++currentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return car;
    }

    @Override
    public Car getById(long id) {
        String sql = "SELECT * FROM cars WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Car car = new Car(rs.getString("brand"), rs.getBigDecimal("price"), rs.getInt("year"));
                car.setId(rs.getLong("id"));
                return car;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car update(long id, BigDecimal price) {
        String sql = "UPDATE cars SET price = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, price);
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Car update(Car car) {
        String sql = "UPDATE cars SET price = ? WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, car.getPrice());
            pstmt.setLong(2, car.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM cars WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void createData() {
        String sql = "CREATE TABLE IF NOT EXISTS cars (id SERIAL PRIMARY KEY, brand VARCHAR(50) NOT NULL, price NUMERIC(10,2) NOT NULL, year INT NOT NULL );";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            initData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
