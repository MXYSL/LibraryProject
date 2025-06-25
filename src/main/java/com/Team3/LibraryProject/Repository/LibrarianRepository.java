package com.Team3.LibraryProject.Repository;

import com.Team3.LibraryProject.Entity.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LibrarianRepository extends JpaRepository<Librarian, String> {
    @Query("SELECT MAX(CAST(SUBSTRING(l.id, 2) AS int)) FROM Librarian l")
    Integer findLastLibrarianNumber();
}