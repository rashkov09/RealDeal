package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.JsonDto.PictureSeedDto;
import softuni.exam.models.entity.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.PictureService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class PictureServiceImpl implements PictureService {
    private final static String PICTURES_DATE_FILE_PATH = "src/main/resources/files/json/pictures.json";

    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final CarService carService;

    public PictureServiceImpl(PictureRepository pictureRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, CarService carService) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.carService = carService;
    }

    @Override
    public boolean areImported() {
        return pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return Files.readString(Path.of(PICTURES_DATE_FILE_PATH));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder builder = new StringBuilder();

        PictureSeedDto[] pictureSeedDtos = gson.fromJson(readPicturesFromFile(), PictureSeedDto[].class);

        Arrays.stream(pictureSeedDtos)
                .forEach(pictureSeedDto -> {
                    boolean valid = validationUtil.isValid(pictureSeedDto);
                    if (valid) {
                        Picture picture = modelMapper.map(pictureSeedDto, Picture.class);
                        picture.setCar(carService.getCarById(pictureSeedDto.getCar()));
                        pictureRepository.save(picture);
                        builder.append(String.format("Successfully import picture - %s", pictureSeedDto.getName()));
                    } else {
                        builder.append("Invalid picture");
                    }
                    builder.append(System.lineSeparator());
                });

        return builder.toString();
    }
}
