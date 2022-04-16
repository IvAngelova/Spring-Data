package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.AgentSeedDto;
import softuni.exam.models.entity.Agent;
import softuni.exam.repository.AgentRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class AgentServiceImpl implements AgentService {
    private static final String AGENTS_FILE_PATH = "src/main/resources/files/json/agents.json";

    private final AgentRepository agentRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final TownService townService;

    public AgentServiceImpl(AgentRepository agentRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper, TownService townService) {
        this.agentRepository = agentRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return agentRepository.count() > 0;
    }

    @Override
    public String readAgentsFromFile() throws IOException {
        return Files.readString(Path.of(AGENTS_FILE_PATH));
    }

    @Override
    public String importAgents() throws IOException {
        StringBuilder agentsOutputMessage = new StringBuilder();

        AgentSeedDto[] agentSeedDtos = gson.fromJson(readAgentsFromFile(), AgentSeedDto[].class);

        Arrays.stream(agentSeedDtos)
                .filter(agentSeedDto -> {
                    return ensureIsValid(agentsOutputMessage, agentSeedDto);
                })
                .map(agentSeedDto -> {
                    Agent agent = modelMapper.map(agentSeedDto, Agent.class);
                    agent.setTown(townService.findTownByName(agentSeedDto.getTown()));
                    return agent;
                })
                .forEach(agentRepository::save);


        return agentsOutputMessage.toString();
    }

    @Override
    public Agent findByFirstName(String name) {
        return agentRepository.findAgentByFirstName(name);
    }

    private boolean ensureIsValid(StringBuilder agentsOutputMessage, AgentSeedDto agentSeedDto) {
        boolean isValid = validationUtil.isValid(agentSeedDto) &&
                !agentRepository.existsByFirstName(agentSeedDto.getFirstName());
        if (isValid) {
            agentsOutputMessage.append(String.format("Successfully imported agent %s %s",
                    agentSeedDto.getFirstName(), agentSeedDto.getLastName()));
        } else {
            agentsOutputMessage.append("Invalid agent");
        }

        agentsOutputMessage.append(System.lineSeparator());

        return isValid;
    }
}
