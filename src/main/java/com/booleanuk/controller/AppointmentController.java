package com.booleanuk.controller;

import com.booleanuk.model.Appointment;
import com.booleanuk.model.AppointmentDTO;
import com.booleanuk.model.Doctor;
import com.booleanuk.model.Patient;
import com.booleanuk.repository.AppointmentRepository;
import com.booleanuk.repository.DoctorRepository;
import com.booleanuk.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    PatientRepository patientRepository;

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAll() {
        List<Appointment> appointments = this.appointmentRepository.findAll();
        ArrayList<AppointmentDTO> appointmentDTOS = new ArrayList<>();

        appointments.stream()
                .forEach(a -> appointmentDTOS.add(convertToDTO(a)));

        return ResponseEntity.ok(appointmentDTOS);
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> addAppointment(@RequestBody Appointment appointment) {
        Doctor doctor = this.doctorRepository
                .findById(appointment.getDoctor().getId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such doctor"));

        Patient patient = this.patientRepository
                .findById(appointment.getPatient().getId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such patient."));

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        this.appointmentRepository.save(appointment);

        return new ResponseEntity<>(convertToDTO(appointment), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable (name = "id") int id) {
        Appointment appointment = this.appointmentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such appointment exists"));
        return new ResponseEntity<>(convertToDTO(appointment), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable (name = "id") int id, @RequestBody Appointment appointment){
        Appointment appointmentToUpdate = getAppointment(id);

        try {
            if (appointmentToUpdate.getAppointmentTime() != appointment.getAppointmentTime()){
                appointmentToUpdate.setAppointmentTime(appointment.getAppointmentTime());
            }

            if (appointmentToUpdate.getDoctor().getId() != appointment.getDoctor().getId()){
                Doctor newDoctor = appointment.getDoctor();
                appointmentToUpdate.setDoctor(newDoctor);
            }

            if (appointmentToUpdate.getPatient().getId() != appointment.getPatient().getId()){
                Patient newPatient = appointment.getPatient();
                appointmentToUpdate.setPatient(newPatient);
            }
            this.appointmentRepository.save(appointmentToUpdate);

            return new ResponseEntity<>(convertToDTO(appointmentToUpdate), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An error occurred when attempting to update publisher: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppointmentDTO> deleteAppointment(@PathVariable (name = "id") int id){
        Appointment appointmentToDelete = getAppointment(id);
        this.appointmentRepository.delete(appointmentToDelete);
        return new ResponseEntity<>(convertToDTO(appointmentToDelete), HttpStatus.OK);
    }

    private Appointment getAppointment(int id) throws ResponseStatusException {
        return this.appointmentRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such appointment."));
    }

    private AppointmentDTO convertToDTO(Appointment appointment){
        return new AppointmentDTO(
                appointment.getPatient().getName(),
                appointment.getDoctor().getName(),
                appointment.getAppointmentTime());
    }
}

