package com.example.springintro.repository;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.EditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByReleaseDateAfter(LocalDate releaseDateAfter);

    List<Book> findAllByReleaseDateBefore(LocalDate releaseDateBefore);

    List<Book> findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(String author_firstName, String author_lastName);

    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);

    List<Book> findAllByEditionTypeAndCopiesLessThan(EditionType editionType, Integer copies);

    List<Book> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal lower, BigDecimal upper);

    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate lower, LocalDate upper);

    List<Book> findAllByTitleContaining(String containStr);

    List<Book> findAllByAuthor_LastNameStartingWith(String prefix);

    @Query("SELECT count(b) FROM Book b WHERE length(b.title) > :length")
    int getCountOfBooksWithTitleLengthGreaterThan(@Param("length") int minLength);

    Book findBookByTitle(String title);

    @Modifying
    @Query("UPDATE Book b SET b.copies = b.copies + :numberCopies WHERE b.releaseDate > :date")
    int updateCopiesByReleaseDate(@Param("date") LocalDate releaseDate,
                                  @Param("numberCopies") int copies);

    @Modifying
    int deleteBooksByCopiesLessThan(int amount);

    @Procedure("usp_total_books_by_author")
    int getCountBooksByAuthorName(String firstName, String lastName);


}

