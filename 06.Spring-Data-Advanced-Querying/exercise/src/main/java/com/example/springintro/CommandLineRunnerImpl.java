package com.example.springintro;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.Scanner;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final Scanner scanner;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService, Scanner scanner) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.scanner = scanner;
    }

    @Override
    public void run(String... args) throws Exception {

        seedData();

        System.out.println("Please, enter exercise number: ");
        int exerciseNumber = Integer.parseInt(scanner.nextLine());

        switch (exerciseNumber) {
            case 1 -> booksTitlesByAgeRestriction();
            case 2 -> goldenBooks();
            case 3 -> booksByPrice();
            case 4 -> notReleasedBooks();
            case 5 -> booksReleasedBeforeDate();
            case 6 -> authorsSearch();
            case 7 -> booksSearch();
            case 8 -> BookTitlesSearch();
            case 9 -> countBook();
            case 10 -> totalBookCopies();
            case 11 -> reducedBook();
            case 12 -> increaseBookCopies();
            case 13 -> removeBooks();
            case 14 -> storedProcedure();

        }

        //printAllBooksAfterYear(2000);
        //printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(1990);
        //printAllAuthorsAndNumberOfTheirBooks();
        //printALlBooksByAuthorNameOrderByReleaseDate("George", "Powell");

    }

    private void storedProcedure() {
        //SQL script for creating the stored procedure in resources package!
        System.out.println("Please, enter full name: ");
        String fullName = scanner.nextLine().trim();
        int countOfBooks = bookService.getCountOfBooksByAuthor(fullName);
        System.out.printf("%s has written %d books%n", fullName, countOfBooks);
    }

    private void removeBooks() {
        int amount = Integer.parseInt(scanner.nextLine());
        int deleteCount = this.bookService.deleteWithCopiesLessThan(amount);
        System.out.println(deleteCount + " books were deleted.");
    }

    private void increaseBookCopies() {
        System.out.println("Please, enter date in format dd MMM yyyy: ");
        String date = scanner.nextLine();
        System.out.println("Please, enter number of copies: ");
        int copies = Integer.parseInt(scanner.nextLine());

        int totalNumberOfCopiesAdded = bookService.increaseBookCopiesByDate(date, copies);
        System.out.println(totalNumberOfCopiesAdded);
    }

    private void reducedBook() {
        System.out.println("Please, enter title: ");
        String title = scanner.nextLine();
        Book searchedBook = bookService.findBookByGivenTitle(title);
        System.out.printf("%s %s %s %.2f%n", searchedBook.getTitle(), searchedBook.getEditionType(),
                searchedBook.getAgeRestriction(), searchedBook.getPrice());
    }

    private void totalBookCopies() {
        this.authorService.findAllAuthorsAndTheirTotalCopies()
                .forEach(a -> System.out.println(
                        a.getFirstName() + " " + a.getLastName() +
                                " - " + a.getTotalCopies()));
    }

    private void countBook() {
        System.out.println("Please, enter min length of title: ");
        int length = Integer.parseInt(scanner.nextLine());
        System.out.println(bookService.getCountOfBooksWithTitleLongerThan(length));
    }

    private void BookTitlesSearch() {
        System.out.println("Please, enter author last name prefix: ");
        String prefix = scanner.nextLine();
        bookService.findAllBooksWithAuthorLastNamePrefix(prefix).forEach(System.out::println);
    }

    private void booksSearch() {
        System.out.println("Please, enter a string: ");
        String containString = scanner.nextLine();
        bookService.findAllBookTitlesWhichContainAString(containString).forEach(System.out::println);
    }

    private void authorsSearch() {
        System.out.println("Please, enter first name suffix: ");
        String suffix = scanner.nextLine();
        authorService.findAllAuthorNamesWithSuffix(suffix).forEach(System.out::println);
    }

    private void booksReleasedBeforeDate() {
        System.out.println("Please, enter a date in format dd-MM-yyyy: ");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        bookService.findAllBooksBeforeDate(date).forEach(System.out::println);
    }

    private void notReleasedBooks() {
        System.out.println("Please, enter year: ");
        int year = Integer.parseInt(scanner.nextLine());
        bookService.findAllBooksThatAreNotReleasedIn(year).forEach(System.out::println);

    }

    private void booksByPrice() {
        bookService.findAllBookTitlesByPrice().forEach(System.out::println);
    }

    private void goldenBooks() {
        bookService.findAllGoldBookTitlesWithCopiesLessThan5000().forEach(System.out::println);
    }

    private void booksTitlesByAgeRestriction() {
        System.out.println("Please, enter age restriction: ");
        AgeRestriction ageRestriction = AgeRestriction.valueOf(scanner.nextLine().toUpperCase());
        bookService.findAllBookTitlesWithAgeRestriction(ageRestriction)
                .forEach(System.out::println);
    }

    private void printALlBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService
                .findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        authorService
                .getAllAuthorsOrderByCountOfTheirBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year) {
        bookService
                .findAllAuthorsWithBooksWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfterYear(int year) {
        bookService
                .findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}
