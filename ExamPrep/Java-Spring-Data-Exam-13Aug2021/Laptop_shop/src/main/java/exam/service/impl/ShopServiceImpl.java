package exam.service.impl;

import exam.model.dto.ShopSeedDto;
import exam.model.dto.ShopSeedRootDto;
import exam.model.entity.Shop;
import exam.repository.ShopRepository;
import exam.service.ShopService;
import exam.service.TownService;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ShopServiceImpl implements ShopService {
    private static final String SHOPS_FILE_PATH = "src/main/resources/files/xml/shops.xml";

    private final ShopRepository shopRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final TownService townService;

    public ShopServiceImpl(ShopRepository shopRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil, TownService townService) {
        this.shopRepository = shopRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return Files.readString(Path.of(SHOPS_FILE_PATH));
    }

    @Override
    public String importShops() throws JAXBException, FileNotFoundException {
        StringBuilder shopsOutputInfo = new StringBuilder();

        ShopSeedRootDto shopSeedRootDto = xmlParser.fromFile(SHOPS_FILE_PATH, ShopSeedRootDto.class);
        shopSeedRootDto.getShops()
                .stream()
                .filter(shopSeedDto -> {
                    return ensureIsValid(shopsOutputInfo, shopSeedDto);
                })
                .map(shopSeedDto -> {
                    Shop shop = modelMapper.map(shopSeedDto, Shop.class);
                    shop.setTown(townService.findTownByName(shopSeedDto.getTown().getName()));
                    return shop;
                })
                .forEach(shopRepository::save);

        return shopsOutputInfo.toString();
    }

    @Override
    public Shop findShopByName(String name) {
        return shopRepository.findByName(name);
    }

    private boolean ensureIsValid(StringBuilder shopsOutputInfo, ShopSeedDto shopSeedDto) {
        if (validationUtil.isValid(shopSeedDto) && !shopRepository.existsTownByName(shopSeedDto.getName())) {
            shopsOutputInfo.append(String.format("Successfully imported Shop %s - %.0f",
                            shopSeedDto.getName(), shopSeedDto.getIncome()))
                    .append(System.lineSeparator());
            return true;
        }

        shopsOutputInfo.append("Invalid Shop").append(System.lineSeparator());
        return false;
    }
}
