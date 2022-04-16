package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferSeedDto;
import softuni.exam.models.dto.OfferSeedRootDto;
import softuni.exam.models.entity.ApartmentType;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.OfferService;
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
    private final AgentService agentService;
    private final ApartmentService apartmentService;

    public OfferServiceImpl(OfferRepository offerRepository, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper, AgentService agentService, ApartmentService apartmentService) {
        this.offerRepository = offerRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.agentService = agentService;
        this.apartmentService = apartmentService;
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
        StringBuilder offersOutputMessage = new StringBuilder();

        OfferSeedRootDto offerSeedRootDto = xmlParser.fromFile(OFFERS_FILE_PATH, OfferSeedRootDto.class);
        offerSeedRootDto.getOffers()
                .stream()
                .filter(offerSeedDto -> {
                    return ensureIsValid(offersOutputMessage, offerSeedDto);
                })
                .map(offerSeedDto -> {
                    Offer offer = modelMapper.map(offerSeedDto, Offer.class);
                    offer.setAgent(agentService.findByFirstName(offerSeedDto.getAgent().getName()));
                    offer.setApartment(apartmentService.findApartmentById(offerSeedDto.getApartment().getId()));
                    return offer;
                })
                .forEach(offerRepository::save);

        return offersOutputMessage.toString();
    }

    private boolean ensureIsValid(StringBuilder offersOutputMessage, OfferSeedDto offerSeedDto) {
        boolean isValid = validationUtil.isValid(offerSeedDto) &&
                (agentService.findByFirstName(offerSeedDto.getAgent().getName()) != null);
        if (isValid) {
            offersOutputMessage.append(String.format("Successfully imported offer %.2f",
                    offerSeedDto.getPrice()));
        } else {
            offersOutputMessage.append("Invalid offer");
        }

        offersOutputMessage.append(System.lineSeparator());

        return isValid;
    }

    @Override
    public String exportOffers() {
        StringBuilder outputBestOffers = new StringBuilder();

        offerRepository.findAllOffersOfThreeRoomsApartmentsOrderByAreaDescThenByPrice(ApartmentType.three_rooms)
                .forEach(offer -> {
                    outputBestOffers.append(String.format("Agent %s %s with offer №%d:\n" +
                            "\t-Apartment area: %.2f\n" +
                            "\t--Town: %s\n" +
                            "\t---Price: %.2f$\n",
                            offer.getAgent().getFirstName(), offer.getAgent().getLastName(),
                            offer.getId(), offer.getApartment().getArea(), offer.getApartment().getTown().getTownName(),
                            offer.getPrice()));
                });
        return outputBestOffers.toString();
    }
}
