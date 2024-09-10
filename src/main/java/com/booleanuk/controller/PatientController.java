package com.booleanuk.controller;

import com.booleanuk.model.Patient;
import com.booleanuk.model.PatientDTO;
import com.booleanuk.repository.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    PatientRepository patientRepository;

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients(){
        ArrayList<PatientDTO> patientDTOAppointments = new ArrayList<>();
        this.patientRepository.findAll().forEach(a -> patientDTOAppointments.add(convertToPatientDTO(a)));
        return new ResponseEntity<>(patientDTOAppointments, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PatientDTO> addPatient(@RequestBody Patient patient) throws ResponseStatusException {
        try {
            this.patientRepository.save(patient);
            return new ResponseEntity<>(convertToPatientDTO(patient), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create patient: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable (name = "id") int id) throws ResponseStatusException {
        Patient patient = this.patientRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No patient with the provided id"));

        return ResponseEntity.ok(convertToPatientDTO(patient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable (name = "id") int id, @RequestBody Patient patient) throws ResponseStatusException {
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

            return new ResponseEntity<>(convertToPatientDTO(patientToUpdate), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An error occurred when attempting to update patient: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PatientDTO> deletePatient(@PathVariable (name = "id") int id) throws ResponseStatusException {
        Patient patientToDelete = this.patientRepository.
                findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No patient with the provided id was found"));
        this.patientRepository.delete(patientToDelete);

        return new ResponseEntity<>(convertToPatientDTO(patientToDelete), HttpStatus.OK);
    }

    private PatientDTO convertToPatientDTO(Patient patient){
        int id = patient.getId();
        String name = patient.getName();
        Date dateOfBirth = patient.getDateOfBirth();
        ArrayList<String> appointments = new ArrayList<>();
        if (patient.getAppointments() != null){
            patient.getAppointments().forEach(a -> appointments.add(a.getDoctor().getName()));
        }

        return new PatientDTO(id, name, dateOfBirth, appointments);
    }
}
