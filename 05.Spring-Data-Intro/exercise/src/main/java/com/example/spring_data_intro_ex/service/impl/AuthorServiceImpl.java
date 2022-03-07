package com.example.spring_data_intro_ex.service.impl;

import com.example.spring_data_intro_ex.model.entity.Author;
import com.example.spring_data_intro_ex.repository.AuthorRepository;
import com.example.spring_data_intro_ex.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {
    private static final String AUTHORS_FILE_PATH = "src/main/resources/files/authors.txt";
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    @Override
    public void seedAuthors() throws IOException {
        if (this.authorRepository.count() > 0) {
            return;
        }
        Files.readAllLines(Path.of(AUTHORS_FILE_PATH))
                .forEach(authorName -> {
                    String[] fullName = authorName.split("\\s+");
                    Author author = new Author(fullName[0], fullName[1]);
                    this.authorRepository.save(author);
                });
    }

    @Override
    public Author getRandomAuthor() {
        long randomAuthorId = ThreadLocalRandom.current()
                .nextLong(1, this.authorRepository.count() + 1);

        return this.authorRepository
                .findById(randomAuthorId)
                .orElse(null);

    }

    @Override
    public List<String> getAllAuthorsOrderByBookCount() {
        return this.authorRepository.findAllByBookSizeDESC()
                .stream()
                .map(author -> String.format("%s %s %d", author.getFirstName(), author.getLastName(),
                        author.getBooks().size()))
                .collect(Collectors.toList());
    }
}
