package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PictureSeedDto;
import softuni.exam.models.entity.Car;
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
    private static final String PICTURES_FILE_PATH = "src/main/resources/files/json/pictures.json";
    private final PictureRepository pictureRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final CarService carService;

    public PictureServiceImpl(PictureRepository pictureRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil, CarService carService) {
        this.pictureRepository = pictureRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.carService = carService;
    }

    @Override
    public boolean areImported() {
        return pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return Files.readString(Path.of(PICTURES_FILE_PATH));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder picturesOutputInfo = new StringBuilder();
        PictureSeedDto[] pictureSeedDtos = gson.fromJson(readPicturesFromFile(), PictureSeedDto[].class);
        Arrays.stream(pictureSeedDtos)
                .filter(pictureSeedDto -> {
                    return ensurePictureIsValid(picturesOutputInfo, pictureSeedDto);
                })
                .map(pictureSeedDto -> {
                    Picture picture = modelMapper.map(pictureSeedDto, Picture.class);
                    Car carById = carService.findCarById(pictureSeedDto.getCar());
                    picture.setCar(carById);
                    return picture;
                })
                .forEach(pictureRepository::save);

        return picturesOutputInfo.toString();
    }

    private boolean ensurePictureIsValid(StringBuilder picturesOutputInfo, PictureSeedDto pictureSeedDto) {
        if (validationUtil.isValid(pictureSeedDto) && pictureRepository
                .findByName(pictureSeedDto.getName()) == null) {
            picturesOutputInfo.append(String.format("Successfully import picture - %s", pictureSeedDto.getName()))
                    .append(System.lineSeparator());
            return true;
        }

        picturesOutputInfo.append("Invalid picture").append(System.lineSeparator());
        return false;
    }
}
