package com.booleanuk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "instructions")
    private String instructions;

    @ManyToMany
    @JoinTable(
            name = "prescription_medicine",
            joinColumns = {@JoinColumn(name = "medicine_id")},
            inverseJoinColumns = {@JoinColumn(name = "prescription_id")}
    )
    @JsonIncludeProperties(value = {"name", "typeOfMedicine"})
    Set<Medicine> medicines = new HashSet<>();

    public Prescription(Integer quantity, String instructions, Set<Medicine> medicines) {
        this.quantity = quantity;
        this.instructions = instructions;
        this.medicines = medicines;
    }

    public void addMedicine(Medicine medicine) {
        medicines.add(medicine);
    }
}
