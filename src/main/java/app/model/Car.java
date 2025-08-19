package app.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Car {
    Long id;
    private String brand;
    private BigDecimal price;
    private int year;

    public Car(String brand, BigDecimal price, int year) {
        this.brand = brand;
        this.price = price;
        this.year = year;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Car() {
    }

    public String getBrand() {
        return brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getYear() {
        return year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Car car)) return false;
        return year == car.year && Objects.equals(id, car.id) && Objects.equals(brand, car.brand) && Objects.equals(price, car.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, price, year);
    }

    @Override
    public String toString() {
        return String.format("Car: id - %s , brand - %s, price - %s, year - %d", id, brand, price, year);
    }
}
