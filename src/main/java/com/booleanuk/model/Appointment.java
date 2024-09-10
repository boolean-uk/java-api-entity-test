package com.booleanuk.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Appointments")
public class Appointment {
    /*
    It is certainly possible to create a composite key, consisting of patient id and doctor id.
    However, this will cause problems as soon as the patient have another appointment with the same doctor.
    I therefor opt not to leverage this strategy.
    */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Removed nullable = false in case patient cancels. Doctor will then have a free appointment.
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    // Removed nullable = false in case doctor can't make it.
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "appointment_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime appointmentTime;
}
