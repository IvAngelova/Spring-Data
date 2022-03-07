package com.example.spring_data_intro_ex.service;

import com.example.spring_data_intro_ex.model.entity.Author;

import java.io.IOException;
import java.util.List;

public interface AuthorService {
    void seedAuthors() throws IOException;

    Author getRandomAuthor();

    List<String> getAllAuthorsOrderByBookCount();
}
