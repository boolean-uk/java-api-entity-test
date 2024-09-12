package com.booleanuk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "Doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;


    @OneToMany(mappedBy = "doctor")
    @JsonIncludeProperties(value = {"appointmentDate", "patient", "online"})
    private List<Appointment> appointments;


    public Doctor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Doctor(String name) {
        this.name = name;
    }

    public Doctor() {
    }


    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }
}
