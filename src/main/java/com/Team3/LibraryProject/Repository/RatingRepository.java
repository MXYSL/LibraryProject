package com.Team3.LibraryProject.Repository;

import com.Team3.LibraryProject.Entity.Rating;
import com.Team3.LibraryProject.Entity.Book;
import com.Team3.LibraryProject.Entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByReaderAndBook(Reader reader, Book book);
    
    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.book = :book")
    Double getAverageRatingForBook(@Param("book") Book book);
    
    List<Rating> findByBook(Book book);
    
    List<Rating> findByReader(Reader reader);
}