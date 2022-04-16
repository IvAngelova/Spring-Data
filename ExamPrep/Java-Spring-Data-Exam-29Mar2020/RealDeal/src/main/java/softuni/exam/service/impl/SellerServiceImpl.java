package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.SellerSeedDto;
import softuni.exam.models.dto.SellerSeedRootDto;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class SellerServiceImpl implements SellerService {
    private static final String SELLERS_FILE_PATH = "src/main/resources/files/xml/sellers.xml";

    private final SellerRepository sellerRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public SellerServiceImpl(SellerRepository sellerRepository, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.sellerRepository = sellerRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files.readString(Path.of(SELLERS_FILE_PATH));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder sellersOutputInfo = new StringBuilder();
        SellerSeedRootDto sellerSeedRootDto = xmlParser.fromFile(SELLERS_FILE_PATH, SellerSeedRootDto.class);

        sellerSeedRootDto.getSellers()
                .stream()
                .filter(sellerSeedDto -> {
                    return ensureIsValid(sellersOutputInfo, sellerSeedDto);
                })
                .map(sellerSeedDto -> modelMapper.map(sellerSeedDto, Seller.class))
                .forEach(sellerRepository::save);


        return sellersOutputInfo.toString();
    }

    private boolean ensureIsValid(StringBuilder sellersOutputInfo, SellerSeedDto sellerSeedDto) {
        boolean isValid = validationUtil.isValid(sellerSeedDto);
        if (isValid) {
            sellersOutputInfo
                    .append(String.format("Successfully imported seller - %s - %s",
                            sellerSeedDto.getLastName(), sellerSeedDto.getEmail()));
        } else {
            sellersOutputInfo.append("Invalid seller");
        }
        sellersOutputInfo.append(System.lineSeparator());

        return isValid;
    }

    @Override
    public Seller findSellerById(Long id) {
        return sellerRepository.findById(id).orElse(null);
    }
}
