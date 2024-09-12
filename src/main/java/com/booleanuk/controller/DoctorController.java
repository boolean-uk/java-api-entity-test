package com.booleanuk.controller;

import com.booleanuk.Response.DoctorListResponse;
import com.booleanuk.Response.DoctorResponse;
import com.booleanuk.Response.ErrorResponse;
import com.booleanuk.Response.Response;
import com.booleanuk.model.Doctor;
import com.booleanuk.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("doctors")
public class DoctorController {

    @Autowired
    DoctorRepository repository;


    @GetMapping
    public ResponseEntity<Response<?>> getAll() {
        List<Doctor> doctors =ResponseEntity.ok(this.repository.findAll()).getBody();
        DoctorListResponse doctorListResponse = new DoctorListResponse();
        doctorListResponse.set(doctors);
        return ResponseEntity.ok(doctorListResponse);
    }


    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getById(@PathVariable Integer id) {
        Doctor doctor = this.repository.findById(id).orElse(null);

        if(doctor == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Doctor not found by ID");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        DoctorResponse doctorResponse = new DoctorResponse();
        doctorResponse.set(doctor);
        return ResponseEntity.ok(doctorResponse);
    }


    @PostMapping
    public ResponseEntity<Response<?>> create(@RequestBody Doctor doctor) {
        if(doctor.getName() == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Invalid name");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        DoctorResponse doctorResponse = new DoctorResponse();
        doctorResponse.set(doctor);
        repository.save(doctor);

        return new ResponseEntity<>(doctorResponse, HttpStatus.CREATED);
    }

}
