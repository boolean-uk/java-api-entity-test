package com.booleanuk.controller;

import com.booleanuk.Response.PatientListResponse;
import com.booleanuk.Response.PatientResponse;
import com.booleanuk.Response.ErrorResponse;
import com.booleanuk.Response.Response;
import com.booleanuk.model.Patient;
import com.booleanuk.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("patients")
public class PatientController {
    @Autowired
    PatientRepository repository;


    @GetMapping
    public ResponseEntity<Response<?>> getAll() {
        List<Patient> Patients =ResponseEntity.ok(this.repository.findAll()).getBody();
        PatientListResponse PatientListResponse = new PatientListResponse();
        PatientListResponse.set(Patients);
        return ResponseEntity.ok(PatientListResponse);
    }


    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getById(@PathVariable Integer id) {
        Patient patient = this.repository.findById(id).orElse(null);

        if(patient == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Patient not found by ID");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        PatientResponse patientResponse = new PatientResponse();
        patientResponse.set(patient);
        return ResponseEntity.ok(patientResponse);
    }


    @PostMapping
    public ResponseEntity<Response<?>> create(@RequestBody Patient patient) {
        if(patient.getFirstName() == null || patient.getLastName() == null || patient.getSsn() == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Invalid name");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        PatientResponse PatientResponse = new PatientResponse();
        PatientResponse.set(patient);
        repository.save(patient);

        return new ResponseEntity<>(PatientResponse, HttpStatus.CREATED);
    }
    
}
