package com.example.springintro.service;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName);

    List<String> findAllBookTitlesWithAgeRestriction(AgeRestriction ageRestriction);

    List<String> findAllGoldBookTitlesWithCopiesLessThan5000();

    List<String> findAllBookTitlesByPrice();

    List<String> findAllBooksThatAreNotReleasedIn(int year);

    List<String> findAllBooksBeforeDate(LocalDate date);

    List<String> findAllBookTitlesWhichContainAString(String containString);

    List<String> findAllBooksWithAuthorLastNamePrefix(String prefix);

    int getCountOfBooksWithTitleLongerThan(int length);

    Book findBookByGivenTitle(String title);

    int increaseBookCopiesByDate(String date, int copies);

    int deleteWithCopiesLessThan(int amount);

    int getCountOfBooksByAuthor(String fullName);
}
