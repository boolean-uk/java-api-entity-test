package com.booleanuk.controller;

import com.booleanuk.Response.AppointmentListResponse;
import com.booleanuk.Response.AppointmentResponse;
import com.booleanuk.Response.ErrorResponse;
import com.booleanuk.Response.Response;
import com.booleanuk.dto.AppointmentDTO;
import com.booleanuk.model.Appointment;
import com.booleanuk.model.Doctor;
import com.booleanuk.model.Patient;
import com.booleanuk.repository.AppointmentRepository;
import com.booleanuk.repository.DoctorRepository;
import com.booleanuk.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("appointments")
public class AppointmentController {

    @Autowired
    AppointmentRepository repository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    PatientRepository patientRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AppointmentDTO appointmentDTO) {
        Patient patient = patientRepository.findById(appointmentDTO.getPatient()).orElse(null);
        Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctor()).orElse(null);

        if(doctor == null || patient == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        Appointment appointment = new Appointment(patient, doctor, appointmentDTO.getAppointmentDate());
        this.repository.save(appointment);

        doctor.addAppointment(appointment);
        patient.addAppointment(appointment);

        AppointmentResponse appointmentResponse = new AppointmentResponse();
        appointmentResponse.set(appointment);

        return ResponseEntity.ok(appointmentResponse);
    }

    @GetMapping
    public ResponseEntity<Response<?>> getAll() {
        List<Appointment> appointments = this.repository.findAll();

        if(appointments.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Appointment list is empty");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        AppointmentListResponse appointmentListResponse = new AppointmentListResponse();
        appointmentListResponse.set(appointments);
        return ResponseEntity.ok(appointmentListResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getById(@PathVariable Integer id) {
        Appointment appointment = this.repository.findById(id).orElse(null);

        if(appointment == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        AppointmentResponse appointmentResponse = new AppointmentResponse();
        appointmentResponse.set(appointment);
        return ResponseEntity.ok(appointmentResponse);
    }
}
