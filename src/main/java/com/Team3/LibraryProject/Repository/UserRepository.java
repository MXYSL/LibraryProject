package com.Team3.LibraryProject.Repository;

import com.Team3.LibraryProject.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByIdAndPassword(String id, String password);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = 'ADMIN'")
    long countAdministrators();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.userType = 'LIBRARIAN'")
    long countLibrarians();
}