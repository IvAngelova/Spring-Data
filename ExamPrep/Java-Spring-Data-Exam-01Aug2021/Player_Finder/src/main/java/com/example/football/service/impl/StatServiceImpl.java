package com.example.football.service.impl;

import com.example.football.models.dto.StatSeedDto;
import com.example.football.models.dto.StatSeedRootDto;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class StatServiceImpl implements StatService {

    private static final String STATS_FILE_PATH = "src/main/resources/files/xml/stats.xml";

    private final StatRepository statRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public StatServiceImpl(StatRepository statRepository, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.statRepository = statRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }


    @Override
    public boolean areImported() {
        return statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return Files.readString(Path.of(STATS_FILE_PATH));
    }

    @Override
    public String importStats() throws JAXBException, FileNotFoundException {
        StringBuilder statsOutputMessage = new StringBuilder();
        StatSeedRootDto statSeedRootDto = xmlParser.fromFile(STATS_FILE_PATH, StatSeedRootDto.class);

        statSeedRootDto.getStats()
                .stream()
                .filter(statSeedDto -> {
                    return ensureIsValid(statsOutputMessage, statSeedDto);
                })
                .map(statSeedDto -> modelMapper.map(statSeedDto, Stat.class))
                .forEach(statRepository::save);

        return statsOutputMessage.toString();
    }

    @Override
    public Stat findStatById(Long id) {
        return statRepository.findById(id).orElse(null);
    }

    private boolean ensureIsValid(StringBuilder statsOutputMessage, StatSeedDto statSeedDto) {
        if (validationUtil.isValid(statSeedDto) &&
                !statRepository.existsStatByPassingAndShootingAndEndurance(statSeedDto.getPassing(),
                        statSeedDto.getShooting(), statSeedDto.getEndurance())) {
            statsOutputMessage.append(String.format("Successfully imported Stat %.2f - %.2f - %.2f",
                            statSeedDto.getShooting(), statSeedDto.getPassing(), statSeedDto.getEndurance()))
                    .append(System.lineSeparator());
            return true;
        }
        statsOutputMessage.append("Invalid Stat").append(System.lineSeparator());
        return false;
    }
}
