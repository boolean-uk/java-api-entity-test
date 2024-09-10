package com.booleanuk.controller;

import com.booleanuk.model.Patient;
import com.booleanuk.repository.PatientRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    PatientRepository patientRepository;

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients(){
        return new ResponseEntity<>(this.patientRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) throws ResponseStatusException {
        try {
            return new ResponseEntity<>(this.patientRepository.save(patient), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create patient: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable (name = "id") int id) throws ResponseStatusException {
        Patient patient = this.patientRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No patient with the provided id"));

        return ResponseEntity.ok(patient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable (name = "id") int id, @RequestBody Patient patient) throws ResponseStatusException {
        Patient patientToUpdate = this.patientRepository.
                findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No patient with the provided id was found"));
        try {

            if (!Objects.equals(patientToUpdate.getName(), patient.getName())){
                patientToUpdate.setName(patient.getName());
            }

            if (!Objects.equals(patientToUpdate.getDateOfBirth(), patient.getDateOfBirth())){
                patientToUpdate.setDateOfBirth(patient.getDateOfBirth());
            }

            return new ResponseEntity<>(patientToUpdate, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An error occurred when attempting to update patient: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Patient> deletePatient(@PathVariable (name = "id") int id) throws ResponseStatusException {
        Patient patientToDelete = this.patientRepository.
                findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No patient with the provided id was found"));
        this.patientRepository.delete(patientToDelete);

        return new ResponseEntity<>(patientToDelete, HttpStatus.OK);
    }
}
