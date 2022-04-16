package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.PassengerSeedDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class PassengerServiceImpl implements PassengerService {

    private static final String PASSENGERS_FILE_PATH = "src/main/resources/files/json/passengers.json";

    private final PassengerRepository passengerRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final TownService townService;

    public PassengerServiceImpl(PassengerRepository passengerRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper, TownService townService) {
        this.passengerRepository = passengerRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return Files.readString(Path.of(PASSENGERS_FILE_PATH));
    }

    @Override
    public String importPassengers() throws IOException {
        StringBuilder passengersOutputInfo = new StringBuilder();

        PassengerSeedDto[] passengerSeedDtos = gson.fromJson(readPassengersFileContent(), PassengerSeedDto[].class);

        Arrays.stream(passengerSeedDtos)
                .filter(passengerSeedDto -> {
                    return ensureIsValid(passengersOutputInfo, passengerSeedDto);
                })
                .map(passengerSeedDto -> {
                    Passenger passenger = modelMapper.map(passengerSeedDto, Passenger.class);
                    passenger.setTown(townService.findByName(passengerSeedDto.getTown()));
                    return passenger;
                })
                .forEach(passengerRepository::save);

        return passengersOutputInfo.toString();
    }

    private boolean ensureIsValid(StringBuilder passengersOutputInfo, PassengerSeedDto passengerSeedDto) {
        boolean isValid = validationUtil.isValid(passengerSeedDto);
        if (isValid) {
            passengersOutputInfo.append(String.format("Successfully imported Passenger %s - %s",
                    passengerSeedDto.getLastName(), passengerSeedDto.getEmail()));
        } else {
            passengersOutputInfo.append("Invalid Passenger");
        }

        passengersOutputInfo.append(System.lineSeparator());
        return isValid;
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder result = new StringBuilder();

        passengerRepository.getAllOrderByTicketsCountThenByEmail()
                .forEach(passenger -> {
                    result.append(String.format("Passenger %s  %s\n" +
                            "\tEmail - %s\n" +
                            "\tPhone - %s\n" +
                            "\tNumber of tickets - %d\n",
                            passenger.getFirstName(), passenger.getLastName(),
                            passenger.getEmail(), passenger.getPhoneNumber(), passenger.getTickets().size()));
                });

        return result.toString();
    }

    @Override
    public Passenger findPassengerByEmail(String email) {
        return passengerRepository.findByEmail(email);
    }
}
