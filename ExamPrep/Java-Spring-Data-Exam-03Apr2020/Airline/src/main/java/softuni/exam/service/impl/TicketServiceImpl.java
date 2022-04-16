package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.TicketSeedDto;
import softuni.exam.models.dtos.TicketSeedRootDto;
import softuni.exam.models.entities.Ticket;
import softuni.exam.repository.TicketRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.PlaneService;
import softuni.exam.service.TicketService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TicketServiceImpl implements TicketService {
    private static final String TICKETS_FILE_PATH = "src/main/resources/files/xml/tickets.xml";

    private final TicketRepository ticketRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final TownService townService;
    private final PassengerService passengerService;
    private final PlaneService planeService;

    public TicketServiceImpl(TicketRepository ticketRepository, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper, TownService townService, PassengerService passengerService, PlaneService planeService) {
        this.ticketRepository = ticketRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.townService = townService;
        this.passengerService = passengerService;
        this.planeService = planeService;
    }

    @Override
    public boolean areImported() {
        return ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files.readString(Path.of(TICKETS_FILE_PATH));
    }

    @Override
    public String importTickets() throws JAXBException, FileNotFoundException {
        StringBuilder ticketsOutputInfo = new StringBuilder();

        TicketSeedRootDto ticketSeedRootDto = xmlParser.fromFile(TICKETS_FILE_PATH, TicketSeedRootDto.class);

        ticketSeedRootDto.getTickets()
                .stream()
                .filter(ticketSeedDto -> {
                    return ensureIsValid(ticketsOutputInfo, ticketSeedDto);
                })
                .map(ticketSeedDto -> {
                    return mapToTicketEntity(ticketSeedDto);
                })
                .forEach(ticketRepository::save);

        return ticketsOutputInfo.toString();
    }

    private Ticket mapToTicketEntity(TicketSeedDto ticketSeedDto) {
        Ticket ticket = modelMapper.map(ticketSeedDto, Ticket.class);
        ticket.setFromTown(townService.findByName(ticketSeedDto.getFromTown().getName()));
        ticket.setToTown(townService.findByName(ticketSeedDto.getToTown().getName()));
        ticket.setPlane(planeService.findPlaneByRegisterNumber(ticketSeedDto.getPlane().getRegisterNumber()));
        ticket.setPassenger(passengerService.findPassengerByEmail(ticketSeedDto.getPassenger().getEmail()));
        return ticket;
    }

    private boolean ensureIsValid(StringBuilder ticketsOutputInfo, TicketSeedDto ticketSeedDto) {
        if (validationUtil.isValid(ticketSeedDto) &&
                ticketRepository.findTicketBySerialNumber(ticketSeedDto.getSerialNumber()) == null) {
            ticketsOutputInfo.append(String.format("Successfully imported Ticket %s - %s",
                            ticketSeedDto.getFromTown().getName(), ticketSeedDto.getToTown().getName()))
                    .append(System.lineSeparator());
            return true;
        }

        ticketsOutputInfo.append("Invalid Ticket").append(System.lineSeparator());
        return false;
    }
}
