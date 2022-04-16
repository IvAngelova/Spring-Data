package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.PostSeedDto;
import softuni.exam.instagraphlite.models.dtos.PostSeedRootDto;
import softuni.exam.instagraphlite.models.entities.Picture;
import softuni.exam.instagraphlite.models.entities.Post;
import softuni.exam.instagraphlite.models.entities.User;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.PostService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;
import softuni.exam.instagraphlite.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PostServiceImpl implements PostService {
    private static final String POSTS_FILE_PATH = "src/main/resources/files/posts.xml";

    private final PostRepository postRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final PictureService pictureService;
    private final UserService userService;

    public PostServiceImpl(PostRepository postRepository, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper, PictureService pictureService, UserService userService) {
        this.postRepository = postRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.pictureService = pictureService;
        this.userService = userService;
    }

    @Override
    public boolean areImported() {
        return postRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(POSTS_FILE_PATH));
    }

    @Override
    public String importPosts() throws IOException, JAXBException {
        StringBuilder postsOutputInfo = new StringBuilder();

        PostSeedRootDto postSeedRootDto = xmlParser.fromFile(POSTS_FILE_PATH, PostSeedRootDto.class);
        for (PostSeedDto postSeedDto : postSeedRootDto.getPosts()) {

            String picturePath = postSeedDto.getPicture().getPath();
            Picture pictureByPath = pictureService.findPictureByPath(picturePath);
            String username = postSeedDto.getUser().getUsername();
            User userByUsername = userService.findUserByUsername(username);
            if (pictureByPath == null || userByUsername == null) {
                postsOutputInfo.append("Invalid Post").append(System.lineSeparator());
                continue;
            }

            boolean isValid = validationUtil.isValid(postSeedDto);
            if (isValid) {
                postsOutputInfo.append(String.format("Successfully imported Post, made by %s",
                                postSeedDto.getUser().getUsername()))
                        .append(System.lineSeparator());
            } else {
                postsOutputInfo.append("Invalid Post").append(System.lineSeparator());
                continue;
            }

            Post post = modelMapper.map(postSeedDto, Post.class);
            post.setPicture(pictureByPath);
            post.setUser(userByUsername);
            postRepository.save(post);
        }

        return postsOutputInfo.toString();
    }
}
