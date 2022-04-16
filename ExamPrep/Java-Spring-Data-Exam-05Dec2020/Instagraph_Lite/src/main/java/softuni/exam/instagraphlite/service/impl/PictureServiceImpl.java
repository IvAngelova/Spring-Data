package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.PictureSeedDto;
import softuni.exam.instagraphlite.models.entities.Picture;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class PictureServiceImpl implements PictureService {
    private static final String PICTURES_FILE_PATH = "src/main/resources/files/pictures.json";

    private final PictureRepository pictureRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public PictureServiceImpl(PictureRepository pictureRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.pictureRepository = pictureRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }


    @Override
    public boolean areImported() {
        return pictureRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(PICTURES_FILE_PATH));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder picturesOutputInfo = new StringBuilder();

        PictureSeedDto[] pictureSeedDtos = gson.fromJson(readFromFileContent(), PictureSeedDto[].class);

        Arrays.stream(pictureSeedDtos)
                .filter(pictureSeedDto -> {
                    return ensureIsValid(picturesOutputInfo, pictureSeedDto);
                })
                .map(pictureSeedDto -> modelMapper.map(pictureSeedDto, Picture.class))
                .forEach(pictureRepository::save);


        return picturesOutputInfo.toString();
    }

    private boolean ensureIsValid(StringBuilder picturesOutputInfo, PictureSeedDto pictureSeedDto) {
        if (validationUtil.isValid(pictureSeedDto) &&
                pictureRepository.findByPath(pictureSeedDto.getPath()) == null) {
            picturesOutputInfo.append(String.format("Successfully imported Picture, with size %.2f", pictureSeedDto.getSize()))
                    .append(System.lineSeparator());
            return true;
        }

        picturesOutputInfo.append("Invalid Picture").append(System.lineSeparator());
        return false;
    }

    @Override
    public String exportPictures() {
        StringBuilder output = new StringBuilder();

        pictureRepository.findAllBySizeGreaterThanOrderBySizeAsc(30000.00)
                .forEach(picture -> {
                    output.append(String.format("%.2f – %s", picture.getSize(), picture.getPath()))
                            .append(System.lineSeparator());
                });

        return output.toString();
    }

    @Override
    public Picture findPictureByPath(String path) {
        return pictureRepository.findByPath(path);
    }
}
