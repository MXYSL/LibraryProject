package com.Team3.LibraryProject.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@DiscriminatorValue("ADMIN")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Administrator extends User {
    @Column(name = "employee_number")
    private String employeeNumber;
    
    @Column
    private Integer age;
    
    @Column
    private String address;
    
    @Column(name = "ine_folio")
    private String ineFolio;
    
    @Column
    private String rfc;
    
    @Column
    private String curp;
    
    @Column(name = "marital_status")
    private String maritalStatus;
}
