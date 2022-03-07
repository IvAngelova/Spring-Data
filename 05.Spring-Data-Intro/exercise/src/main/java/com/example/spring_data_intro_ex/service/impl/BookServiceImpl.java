package com.example.spring_data_intro_ex.service.impl;

import com.example.spring_data_intro_ex.model.entity.*;
import com.example.spring_data_intro_ex.repository.BookRepository;
import com.example.spring_data_intro_ex.service.AuthorService;
import com.example.spring_data_intro_ex.service.BookService;
import com.example.spring_data_intro_ex.service.CategoryService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private static final String BOOKS_FILE_PATH = "src/main/resources/files/books.txt";
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }


    @Override
    public void seedBooks() throws IOException {
        if (this.bookRepository.count() > 0) {
            return;
        }

        Files.readAllLines(Path.of(BOOKS_FILE_PATH))
                .forEach(bookInfo -> {
                    String[] bookData = bookInfo.split("\\s+");

                    Book book = createBookFromData(bookData);

                    this.bookRepository.save(book);
                });
    }

    @Override
    public List<Book> findAllBooksAfterYear(int year) {
      return  this.bookRepository
              .findAllByReleaseDateAfter(LocalDate.of(year, 12, 31));

    }

    @Override
    public List<String> getAllAuthorNamesWithBooksWithReleaseDateBeforeYear(int year) {
        return this.bookRepository
                .findAllByReleaseDateBefore(LocalDate.of(year, 1,1))
                .stream()
                .map(book -> String.format("%s %s", book.getAuthor().getFirstName(),
                        book.getAuthor().getLastName()))
                .distinct()
                .collect(Collectors.toList());

    }

    @Override
    public List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName) {
        return this.bookRepository.findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(firstName,lastName)
                .stream()
                .map(book -> String.format("%s %s %d", book.getTitle(), book.getReleaseDate(), book.getCopies()))
                .collect(Collectors.toList());
    }

    private Book createBookFromData(String[] bookData) {
        EditionType editionType = EditionType.values()[Integer.parseInt(bookData[0])];
        LocalDate releaseDate = LocalDate.parse(bookData[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
        int copies = Integer.parseInt(bookData[2]);
        BigDecimal price = new BigDecimal(bookData[3]);
        AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(bookData[4])];
        String title = Arrays.stream(bookData)
                .skip(5)
                .collect(Collectors.joining(" "));

        Author author = this.authorService.getRandomAuthor();
        Set<Category> categories = this.categoryService.getRandomCategories();

        return new Book(title, editionType, price, releaseDate,
                ageRestriction, author, categories, copies);

    }
}
