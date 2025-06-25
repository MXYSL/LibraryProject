package com.Team3.LibraryProject.Repository;

import com.Team3.LibraryProject.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByPublisherContainingIgnoreCase(String publisher);
    List<Book> findByEditionContainingIgnoreCase(String edition);
    List<Book> findByIsbn(String isbn);
    
    @Query("SELECT b FROM Book b WHERE b.genre.id = :genreId")
    List<Book> findByGenreId(@Param("genreId") Long genreId);
    
    @Query("SELECT b FROM Book b WHERE b.availableQuantity > 0")
    List<Book> findAvailableBooks();
    
    @Query("SELECT b FROM Book b WHERE " +
           "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
           "(:publisher IS NULL OR LOWER(b.publisher) LIKE LOWER(CONCAT('%', :publisher, '%'))) AND " +
           "(:edition IS NULL OR LOWER(b.edition) LIKE LOWER(CONCAT('%', :edition, '%'))) AND " +
           "(:genreId IS NULL OR b.genre.id = :genreId)")
    List<Book> findByAdvancedSearch(@Param("title") String title,
                                  @Param("author") String author,
                                  @Param("publisher") String publisher,
                                  @Param("edition") String edition,
                                  @Param("genreId") Long genreId);
}