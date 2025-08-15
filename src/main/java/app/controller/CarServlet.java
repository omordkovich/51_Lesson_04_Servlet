package app.controller;

import app.repository.CarRepositoryMap;
import app.repository.ICarRepository;
import jakarta.servlet.http.HttpServlet;

public class CarServlet extends HttpServlet {

    private ICarRepository repository = new CarRepositoryMap();

    //GET http://10.2.3.4;8080/cars
}
