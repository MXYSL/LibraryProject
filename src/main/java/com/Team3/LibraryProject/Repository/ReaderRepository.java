package com.Team3.LibraryProject.Repository;

import com.Team3.LibraryProject.Entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, String> {
    Optional<Reader> findByLibraryCredential(String credential);
    
    @Query("SELECT r FROM Reader r JOIN r.fines f WHERE f.status = 'PENDING'")
    List<Reader> findReadersWithPendingFines();
    
    @Query("SELECT r FROM Reader r WHERE r.id LIKE 'A%'")
    List<Reader> findAllReaders();
    
    @Query("SELECT MAX(CAST(SUBSTRING(r.id, 2) AS integer)) FROM Reader r WHERE r.id LIKE 'A%'")
    Integer findLastReaderNumber();
}