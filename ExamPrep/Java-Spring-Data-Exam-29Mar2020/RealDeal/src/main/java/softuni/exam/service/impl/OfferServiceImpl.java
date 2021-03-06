package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferSeedDto;
import softuni.exam.models.dto.OfferSeedRootDto;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.OfferService;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class OfferServiceImpl implements OfferService {
    private static final String OFFERS_FILE_PATH = "src/main/resources/files/xml/offers.xml";
    private final OfferRepository offerRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final CarService carService;
    private final SellerService sellerService;

    public OfferServiceImpl(OfferRepository offerRepository, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper, CarService carService, SellerService sellerService) {
        this.offerRepository = offerRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.carService = carService;
        this.sellerService = sellerService;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(OFFERS_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder offersOutputInfo = new StringBuilder();
        OfferSeedRootDto offerSeedRootDto = xmlParser.fromFile(OFFERS_FILE_PATH, OfferSeedRootDto.class);
        offerSeedRootDto.getOffers()
                .stream()
                .filter(offerSeedDto -> {
                    return ensureIsValid(offersOutputInfo, offerSeedDto);
                })
                .map(offerSeedDto -> {
                    Offer offer = modelMapper.map(offerSeedDto, Offer.class);
                    offer.setCar(carService.findCarById(offerSeedDto.getCar().getId()));
                    offer.setSeller(sellerService.findSellerById(offerSeedDto.getSeller().getId()));
                    return offer;
                })
                .forEach(offerRepository::save);
        return offersOutputInfo.toString();
    }

    private boolean ensureIsValid(StringBuilder offersOutputInfo, OfferSeedDto offerSeedDto) {
        boolean isValid = validationUtil.isValid(offerSeedDto);
        if (isValid) {
            offersOutputInfo
                    .append(String.format("Successfully imported offer - %s - %s",
                            offerSeedDto.getAddedOn(), offerSeedDto.getHasGoldStatus()));
        } else {
            offersOutputInfo.append("Invalid offer");
        }
        offersOutputInfo.append(System.lineSeparator());

        return isValid;
    }
}
