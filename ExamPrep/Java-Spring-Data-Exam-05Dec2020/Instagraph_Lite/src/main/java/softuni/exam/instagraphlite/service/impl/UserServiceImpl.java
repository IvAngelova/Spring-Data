package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.UserSeedDto;
import softuni.exam.instagraphlite.models.entities.Picture;
import softuni.exam.instagraphlite.models.entities.User;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Service
public class UserServiceImpl implements UserService {
    private static final String USERS_FILE_PATH = "src/main/resources/files/users.json";

    private final UserRepository userRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final PictureService pictureService;

    public UserServiceImpl(UserRepository userRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper, PictureService pictureService) {
        this.userRepository = userRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.pictureService = pictureService;
    }

    @Override
    public boolean areImported() {
        return userRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(USERS_FILE_PATH));
    }

    @Override
    public String importUsers() throws IOException {
        StringBuilder usersOutputInfo = new StringBuilder();

        UserSeedDto[] userSeedDtos = gson.fromJson(readFromFileContent(), UserSeedDto[].class);

        for (UserSeedDto userSeedDto : userSeedDtos) {

            String picturePath = userSeedDto.getProfilePicture();
            Picture pictureByPath = pictureService.findPictureByPath(picturePath);
            if (pictureByPath == null) {
                usersOutputInfo.append("Invalid User").append(System.lineSeparator());
                continue;
            }

            boolean isValid = validationUtil.isValid(userSeedDto);
            if (isValid && userRepository.findByUsername(userSeedDto.getUsername()) == null) {
                usersOutputInfo.append(String.format("Successfully imported User: %s", userSeedDto.getUsername()))
                        .append(System.lineSeparator());
            } else {
                usersOutputInfo.append("Invalid User").append(System.lineSeparator());
                continue;
            }

            User user = modelMapper.map(userSeedDto, User.class);
            user.setProfilePicture(pictureByPath);
            userRepository.save(user);
        }

        return usersOutputInfo.toString();
    }

    @Override
    public String exportUsersWithTheirPosts() {
        StringBuilder output = new StringBuilder();

        userRepository.findAllOrderByPostsCountDescThenByIdAsc()
                .forEach(user -> {
                    output.append(String.format("User: %s%n", user.getUsername()));
                    output.append(String.format("Post count: %d%n", user.getPosts().size()));
                    user.getPosts()
                            .stream()
                            .sorted(Comparator.comparingDouble(post -> post.getPicture().getSize()))
                            .forEach(post -> {
                                output.append("==Post Details:").append(System.lineSeparator());
                                output.append(String.format("----Caption: %s%n", post.getCaption()));
                                output.append(String.format("----Picture Size: %.2f%n", post.getPicture().getSize()));
                            });

                });
        return output.toString();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
