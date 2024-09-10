package com.booleanuk.controller;

import com.booleanuk.model.Doctor;
import com.booleanuk.model.Patient;
import com.booleanuk.repository.DoctorRepository;
import com.booleanuk.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.print.Doc;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
    @Autowired
    DoctorRepository doctorRepository;

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors(){
        return new ResponseEntity<>(this.doctorRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Doctor> addDoctor(@RequestBody Doctor doctor) throws ResponseStatusException {
        try {
            return new ResponseEntity<>(this.doctorRepository.save(doctor), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create doctor: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable (name = "id") int id) throws ResponseStatusException {
        Doctor doctor = this.doctorRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No doctor with the provided id"));

        return ResponseEntity.ok(doctor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable (name = "id") int id, @RequestBody Doctor doctor) throws ResponseStatusException {
        Doctor doctorToUpdate = this.doctorRepository.
                findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No doctor with the provided id was found"));
        try {
            if (!Objects.equals(doctorToUpdate.getName(), doctor.getName())){
                doctorToUpdate.setName(doctor.getName());
            }

            return new ResponseEntity<>(this.doctorRepository.save(doctorToUpdate), HttpStatus.CREATED);

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An error occurred when attempting to update doctor: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Doctor> deleteDoctor(@PathVariable (name = "id") int id) throws ResponseStatusException {
        Doctor doctorToDelete = this.doctorRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No doctor with the provided id was found"));
        this.doctorRepository.delete(doctorToDelete);
        return ResponseEntity.ok(doctorToDelete);
    }
}
