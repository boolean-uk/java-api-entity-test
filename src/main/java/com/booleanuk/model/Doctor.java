package com.booleanuk.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "Doctors")
public class Doctor {

    public Doctor(String name){
        this.name = name;
        this.appointments = new ArrayList<>();
    }

    public Doctor(int id){
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;
}