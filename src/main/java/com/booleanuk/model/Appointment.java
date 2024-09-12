package com.booleanuk.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIncludeProperties(value = {"firstName", "lastName", "ssn"})
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonIncludeProperties(value = {"name"})
    private Doctor doctor;

    @Column(name = "appointmentDate")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date appointmentDate;

    @Column(name = "isOnline")
    private boolean isOnline;

    public Appointment(Patient patient, Doctor doctor, Date appointmentDate, boolean isOnline) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.isOnline = isOnline;
    }

}
