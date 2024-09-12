package com.booleanuk.model;

import com.booleanuk.dto.PrescriptionDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="medicines")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "typeOfMedicine")
    private String typeOfMedicine;

    @ManyToMany(mappedBy = "medicines")
    private Set<Prescription> prescriptions = new HashSet<>();

    public Medicine(String name, String typeOfMedicine) {
        this.name = name;
        this.typeOfMedicine = typeOfMedicine;
    }

    public void addPrescriptions(Prescription prescription) {
        this.prescriptions.add(prescription);
    }
}
