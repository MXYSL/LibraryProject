package com.Team3.LibraryProject.Repository;

import com.Team3.LibraryProject.Entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, String> {
    @Query("SELECT MAX(CAST(SUBSTRING(a.id, 2) AS integer)) FROM Administrator a WHERE a.id LIKE 'C%'")
    Integer findLastAdminNumber();
}