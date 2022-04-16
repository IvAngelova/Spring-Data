package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.LaptopSeedDto;
import exam.model.entity.Laptop;
import exam.repository.LaptopRepository;
import exam.service.LaptopService;
import exam.service.ShopService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class LaptopServiceImpl implements LaptopService {
    private static final String LAPTOPS_FILE_PATH = "src/main/resources/files/json/laptops.json";

    private final LaptopRepository laptopRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final ShopService shopService;

    public LaptopServiceImpl(LaptopRepository laptopRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper, ShopService shopService) {
        this.laptopRepository = laptopRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.shopService = shopService;
    }

    @Override
    public boolean areImported() {
        return laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return Files.readString(Path.of(LAPTOPS_FILE_PATH));
    }

    @Override
    public String importLaptops() throws IOException {
        StringBuilder laptopsOutputInfo = new StringBuilder();

        LaptopSeedDto[] laptopSeedDtos = gson.fromJson(readLaptopsFileContent(), LaptopSeedDto[].class);

        Arrays.stream(laptopSeedDtos)
                .filter(laptopSeedDto -> {
                    return ensureIsValid(laptopsOutputInfo, laptopSeedDto);
                })
                .map(laptopSeedDto -> {
                    Laptop laptop = modelMapper.map(laptopSeedDto, Laptop.class);
                    laptop.setShop(shopService.findShopByName(laptopSeedDto.getShop().getName()));
                    return laptop;
                })
                .forEach(laptopRepository::save);

        return laptopsOutputInfo.toString();
    }

    private boolean ensureIsValid(StringBuilder laptopsOutputInfo, LaptopSeedDto laptopSeedDto) {
        if (validationUtil.isValid(laptopSeedDto) && !laptopRepository.existsLaptopByMacAddress(laptopSeedDto.getMacAddress())) {
            laptopsOutputInfo.append(String.format("Successfully imported laptop %s - %.2f - %d - %d",
                            laptopSeedDto.getMacAddress(), laptopSeedDto.getCpuSpeed(), laptopSeedDto.getRam(), laptopSeedDto.getStorage()))
                    .append(System.lineSeparator());
            return true;
        }

        laptopsOutputInfo.append("Invalid laptop").append(System.lineSeparator());
        return false;
    }

    @Override
    public String exportBestLaptops() {
        StringBuilder output = new StringBuilder();

        laptopRepository.getTheBestLaptops()
                .forEach(laptop -> {
                    output.append(String.format("Laptop - %s\n" +
                            "*Cpu speed - %.2f\n" +
                            "**Ram - %d\n" +
                            "***Storage - %d\n" +
                            "****Price - %.2f\n" +
                            "#Shop name - %s\n" +
                            "##Town - %s\n", laptop.getMacAddress(), laptop.getCpuSpeed(),
                            laptop.getRam(), laptop.getStorage(), laptop.getPrice(), laptop.getShop().getName(),
                            laptop.getShop().getTown().getName()))
                            .append(System.lineSeparator());
                });
        return output.toString();
    }
}
