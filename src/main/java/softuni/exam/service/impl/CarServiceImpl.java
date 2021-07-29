package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.JsonDto.CarSeedDto;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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

    StringBuilder builder = new StringBuilder();
        Arrays.stream(gson
                .fromJson(readCarsFileContent(), CarSeedDto[].class))
                .forEach(carSeedDto -> {
                    boolean valid = validationUtil.isValid(carSeedDto);
                    if (valid){
                        Car car = modelMapper.map(carSeedDto,Car.class);
                        carRepository.save(car);
                        builder.append(String.format("Successfully imported car - %s - %s",car.getMake(),car.getModel()));
                    } else {
                        builder.append("Invalid car");
                    }
                    builder.append(System.lineSeparator());
                });


        return builder.toString() ;
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        List<Car>   cars = carRepository.getCarsOrderByPicturesCountThenByMake();

        return cars.stream().map(Car::toString).collect(Collectors.joining("\n"));
    }

    @Override
    public Car getCarById(Long id) {
        return carRepository.findCarById(id);
    }
}
