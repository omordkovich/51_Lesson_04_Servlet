package app.constants;

public interface Constants {

    // http://localhost:8080/cars?id=5
    // jdbc:postgresql://10.2.3.5:5432/c_51_cars?user=my_user&password=pos1234

    String DB_DRIVER_PATH = "org.postgresql.Driver";
    String DB_ADDRESS = "jdbc:postgresql://localhost:5433/";
    String DB_NAME = "c_51_cars";
    String DB_USER = "my_user";
    String DB_PASSWORD = "pos1234";
}
