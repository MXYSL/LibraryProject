package com.Team3.LibraryProject.Service;

import com.Team3.LibraryProject.Entity.*;
import com.Team3.LibraryProject.Entity.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;
import com.Team3.LibraryProject.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ReaderRepository readerRepository;
    
    @Autowired
    private LibrarianRepository librarianRepository; // Corregido: antes era LoanRepository
    
    @Autowired
    private AdministratorRepository administratorRepository;
    
    public Optional<User> authenticateUser(String id, String password) {
        return userRepository.findByIdAndPassword(id, password);
    }
    
    public String generateReaderId() {
        Integer lastNumber = readerRepository.findLastReaderNumber();
        int nextNumber = (lastNumber != null ? lastNumber : 0) + 1;
        return String.format("A%02d", nextNumber);
    }
    
    public String generateLibrarianId() {
        Integer lastNumber = librarianRepository.findLastLibrarianNumber();
        int nextNumber = (lastNumber != null ? lastNumber : 0) + 1;
        return String.format("B%04d", nextNumber);
    }
    
    public String generateAdministratorId() {
        Integer lastNumber = administratorRepository.findLastAdminNumber();
        int nextNumber = (lastNumber != null ? lastNumber : 0) + 1;
        return String.format("C%02d", nextNumber);
    }
    
    public String generateReaderCredential(String name) {
        Integer lastNumber = readerRepository.findLastReaderNumber();
        int nextNumber = (lastNumber != null ? lastNumber : 0) + 1;
        String initials = java.util.Arrays.stream(name.trim().split("\\s+"))
                            .map(s -> s.substring(0, 1).toUpperCase())
                            .reduce("", String::concat);
        return String.format("A-%02d%s", nextNumber, initials);
    }
    
    public String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    public Reader createReader(String name, LocalDate birthDate) {
        Reader reader = new Reader();
        reader.setId(generateReaderId());
        reader.setName(name);
        reader.setBirthDate(birthDate);
        reader.setLibraryCredential(generateReaderCredential(name));
        String generatedPassword = generateRandomPassword(8);
        reader.setPassword(generatedPassword);
        reader.setRegistrationDate(LocalDate.now());
        return readerRepository.save(reader);
    }
    
    public Librarian createLibrarian(String name, int age, String address, String ineFolio, 
                                   String rfc, String curp, String maritalStatus, String workSchedule) {
        // Validar que no exceda el límite de 4 bibliotecarios
        if (librarianRepository.count() >= 4) {
            throw new RuntimeException("No se pueden agregar más bibliotecarios. Límite máximo: 4");
        }
        
        // Validar edad mínima
        if (age < 18) {
            throw new RuntimeException("El bibliotecario debe ser mayor de edad");
        }
        
        Librarian librarian = new Librarian();
        librarian.setId(generateLibrarianId());
        librarian.setName(name);
        librarian.setAge(age);
        librarian.setAddress(address);
        librarian.setIneFolio(ineFolio);
        librarian.setRfc(rfc);
        librarian.setCurp(curp);
        librarian.setMaritalStatus(maritalStatus);
        librarian.setWorkSchedule(workSchedule);
        librarian.setRegistrationDate(LocalDate.now());
        
        return librarianRepository.save(librarian);
    }
    
    public Administrator createAdministrator(String name, int age, String address, String ineFolio,
                                           String rfc, String curp, String maritalStatus) {
        // Validar que no exceda el límite de 2 administradores
        if (administratorRepository.count() >= 2) {
            throw new RuntimeException("No se pueden agregar más administradores. Límite máximo: 2");
        }
        
        Administrator admin = new Administrator();
        admin.setId(generateAdministratorId());
        admin.setName(name);
        admin.setAge(age);
        admin.setAddress(address);
        admin.setIneFolio(ineFolio);
        admin.setRfc(rfc);
        admin.setCurp(curp);
        admin.setMaritalStatus(maritalStatus);
        admin.setRegistrationDate(LocalDate.now());
        
        return administratorRepository.save(admin);
    }
    
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}
