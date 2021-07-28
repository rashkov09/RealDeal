package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CarSeedDto;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CarServiceImpl implements CarService {
    private static final String CARS_DATA_FILE_PATH = "src/main/resources/files/json/cars.json";
    private final CarRepository carRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;


    public CarServiceImpl(CarRepository carRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.carRepository = carRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return Files.readString(Path.of(CARS_DATA_FILE_PATH));
    }

    @Override
    public String importCars() throws IOException {


        Arrays.stream(gson
                .fromJson(readCarsFileContent(), CarSeedDto[].class))
                .filter(validationUtil::isValid)
                .map(carSeedDto -> modelMapper.map(carSeedDto,Car.class))
                .forEach(carRepository::save);


        return "Yes" ;
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        return null;
    }
}
