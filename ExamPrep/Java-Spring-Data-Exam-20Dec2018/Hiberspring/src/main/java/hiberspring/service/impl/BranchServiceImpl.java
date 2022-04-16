package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.domain.dtos.BranchSeedDto;
import hiberspring.domain.entities.Branch;
import hiberspring.repository.BranchRepository;
import hiberspring.service.BranchService;
import hiberspring.service.TownService;
import hiberspring.util.FileUtil;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static hiberspring.common.Constants.*;

@Service
public class BranchServiceImpl implements BranchService {
    private static final String BRANCHES_FILE_NAME = "branches.json";

    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final FileUtil fileUtil;
    private final TownService townService;

    public BranchServiceImpl(BranchRepository branchRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, FileUtil fileUtil, TownService townService) {
        this.branchRepository = branchRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.fileUtil = fileUtil;
        this.townService = townService;
    }

    @Override
    public Boolean branchesAreImported() {
        return branchRepository.count() > 0;
    }

    @Override
    public String readBranchesJsonFile() throws IOException {
        return fileUtil.readFile(PATH_TO_FILES + BRANCHES_FILE_NAME);
    }

    @Override
    public String importBranches(String branchesFileContent) throws IOException {
        StringBuilder sb = new StringBuilder();
        BranchSeedDto[] branchSeedDtos = gson.fromJson(readBranchesJsonFile(), BranchSeedDto[].class);

       Arrays.stream(branchSeedDtos)
                .filter(branchSeedDto -> {
                    boolean isValid = validationUtil.isValid(branchSeedDto);
                    sb.append(isValid
                                    ? String.format(SUCCESSFUL_IMPORT_MESSAGE, "Branch", branchSeedDto.getName())
                                    : INCORRECT_DATA_MESSAGE)
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(branchSeedDto -> {
                    Branch branch = modelMapper.map(branchSeedDto, Branch.class);
                    branch.setTown(townService.findTownByName(branchSeedDto.getTown()));
                    return branch;
                })
                .forEach(branchRepository::save);

        return sb.toString();
    }

    @Override
    public Branch findBranchByName(String name) {
        return branchRepository
                .findByName(name)
                .orElse(null);
    }
}
