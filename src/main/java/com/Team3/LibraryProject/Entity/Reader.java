package com.Team3.LibraryProject.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@DiscriminatorValue("READER")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Reader extends User {
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "library_credential")
    private String libraryCredential;

    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL)
    private List<Loan> loans;

    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL)
    private List<Fine> fines;

    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
}