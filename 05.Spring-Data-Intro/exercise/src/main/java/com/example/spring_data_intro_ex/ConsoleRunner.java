package com.example.spring_data_intro_ex;

import com.example.spring_data_intro_ex.model.entity.Book;
import com.example.spring_data_intro_ex.service.AuthorService;
import com.example.spring_data_intro_ex.service.BookService;
import com.example.spring_data_intro_ex.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public ConsoleRunner(CategoryService categoryService, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
        //printAllBooksAfterYear(2000);
        //printAllAuthorNamesWithBooksWithReleaseDateBeforeYear(1990);
        //printAllAuthorsAndNumberOfTheirBooks();
        printAllBooksByAuthorNameOrderByReleaseDate("George", "Powell");

    }

    private void printAllBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        this.bookService.findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);

    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        this.authorService.getAllAuthorsOrderByBookCount()
                .forEach(System.out::println);
    }

    private void printAllAuthorNamesWithBooksWithReleaseDateBeforeYear(int year) {
        this.bookService.getAllAuthorNamesWithBooksWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);

    }

    private void printAllBooksAfterYear(int year) {
        this.bookService.findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);

    }

    private void seedData() throws IOException {
        this.categoryService.seedCategories();
        this.authorService.seedAuthors();
        this.bookService.seedBooks();
    }
}
