package com.booleanuk.controller;

import com.booleanuk.model.Doctor;
import com.booleanuk.model.DoctorDTO;

import com.booleanuk.repository.AppointmentRepository;
import com.booleanuk.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/doctors")
public class DoctorController {
    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors(){
        ArrayList<DoctorDTO> doctorDTOS = new ArrayList<>();
        this.doctorRepository
                .findAll()
                .forEach(d -> doctorDTOS.add(convertToDoctorDTO(d)));

        return new ResponseEntity<>(doctorDTOS, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DoctorDTO> addDoctor(@RequestBody Doctor doctor) throws ResponseStatusException {
        try {
            this.doctorRepository.save(doctor);
            return new ResponseEntity<>(convertToDoctorDTO(doctor), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create doctor: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable (name = "id") int id) throws ResponseStatusException {
        Doctor doctor = this.doctorRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No doctor with the provided id"));

        return ResponseEntity.ok(convertToDoctorDTO(doctor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable (name = "id") int id, @RequestBody Doctor doctor) throws ResponseStatusException {
        Doctor doctorToUpdate = this.doctorRepository.
                findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No doctor with the provided id was found"));
        try {
            if (!Objects.equals(doctorToUpdate.getName(), doctor.getName())){
                doctorToUpdate.setName(doctor.getName());
            }
            this.doctorRepository.save(doctorToUpdate);
            return new ResponseEntity<>(convertToDoctorDTO(doctorToUpdate), HttpStatus.CREATED);

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An error occurred when attempting to update doctor: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DoctorDTO> deleteDoctor(@PathVariable (name = "id") int id) throws ResponseStatusException {
        Doctor doctorToDelete = this.doctorRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No doctor with the provided id was found"));

        nullifyDoctorAppointments(doctorToDelete);
        this.doctorRepository.delete(doctorToDelete);

        return ResponseEntity.ok(convertToDoctorDTO(doctorToDelete));
    }

    private void nullifyDoctorAppointments(Doctor doctor){
        this.appointmentRepository
                .findAll()
                .stream()
                .filter(a -> a.getDoctor().getId() == doctor.getId())
                .forEach(a -> a.setDoctor(null));
    }

    private DoctorDTO convertToDoctorDTO(Doctor doctor){
        int id = doctor.getId();
        String name = doctor.getName();
        ArrayList<String> appointmentListWithPatientNames = new ArrayList<>();

        if (doctor.getAppointments() != null){
            doctor.getAppointments()
                    .forEach(a -> appointmentListWithPatientNames.add(a.getPatient().getName()));
        }

        return new DoctorDTO(id, name, appointmentListWithPatientNames);
    }
}
