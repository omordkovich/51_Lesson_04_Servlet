package app.controller;

import app.model.Car;
import app.repository.CarRepositoryDB;
import app.repository.CarRepositoryHibernate;
import app.repository.ICarRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class CarServlet extends HttpServlet {
    private final ICarRepository repository = new CarRepositoryHibernate();
    private final ObjectMapper mapper = new ObjectMapper();

    //GET http://10.2.3.4;8080/cars - all cars
    //GET http://10.2.3.4;8080/cars?id=3 - one car by id

    //Ролучает информацию о cars => метод GET
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // /cars=> должны вернуть все автомобили

        //request - Это объект запроса.
        //response - Объект ответа.

        // /example?id?5&name=Cat
        // => { "id" : ["5"], "name" : ["Cat"] }


        Map<String, String[]> params = req.getParameterMap();
        //GET http://10.2.3.4;8080/cars?id=3
        // "id" : ["3"]

        //Установка типа контента для ответа JASON
        resp.setContentType("application/json");
//            BigInteger minPrice = new BigInteger(req.getParameter("minPrice"));


        if (params.isEmpty()) {
            List<Car> cars = repository.getAll();

            //TODO:
            //Homework: minPrice/ sort by Price
            cars = cars.stream()
                    .sorted(Comparator.comparing(Car::getPrice)).toList();

            //Преобразую список в JSON
            String jsonResponse = mapper.writeValueAsString(cars);
            resp.getWriter().write(jsonResponse);

//       //     cars.forEach(car -> {
//                try {
//                    resp.getWriter().write(car.toString() + "\n");
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            });
        } else {
            //TODO есть какието параметры в запросе
            long id = Long.parseLong(params.get("id")[0]);
            Car car = repository.getById(id);

            resp.getWriter().write(car == null ? "Car wasn't found" : mapper.writeValueAsString(car) + "\n");
        }

    }

    //POST /cars - В теле запроса будет приходить объект автомобиля
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //для сохранения нового элемента в БД
        Car car = mapper.readValue(req.getReader(), Car.class);
        repository.save(car);
        resp.setContentType("application/json");
        String jsonResponse = mapper.writeValueAsString(car);
        resp.getWriter().write(jsonResponse);
    }

    @Override
    //PUT /cars
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO: Homework

        // для изменения существующего элемента
        // должна поменять цену
        // id; newPrice

        resp.setContentType("application/json");
        long id = Long.parseLong(req.getParameter("id"));
        BigDecimal newPrice = new BigDecimal(req.getParameter("price"));

        Car updatedCar = repository.update(id, newPrice);

        String json = mapper.writeValueAsString(updatedCar);
        resp.getWriter().write(json);

        //    /        Car car = repository.getById(id);
//        resp.getWriter().write(car == null ? "Car wasn't found" : mapper.writeValueAsString(car) + "\n");
    }

    //TODO: Homework

    //DELETE/cars
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Удаление по id
        long id = Long.parseLong(req.getParameter("id"));
        repository.delete(id);
    }
}
