package com.booleanuk.controller;

import com.booleanuk.Response.ErrorResponse;
import com.booleanuk.Response.PrescriptionListResponse;
import com.booleanuk.Response.PrescriptionResponse;
import com.booleanuk.Response.Response;
import com.booleanuk.dto.PrescriptionDTO;
import com.booleanuk.model.Medicine;
import com.booleanuk.model.Prescription;
import com.booleanuk.repository.MedicineRepository;
import com.booleanuk.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("prescriptions")
public class PrescriptionController {

    @Autowired
    PrescriptionRepository repository;

    @Autowired
    MedicineRepository medicineRepository;

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Prescription> prescriptions = this.repository.findAll();

        if(prescriptions.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Cannot find prescription");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        PrescriptionListResponse prescriptionListResponse = new PrescriptionListResponse();
        prescriptionListResponse.set(prescriptions);
        return ResponseEntity.ok(prescriptionListResponse);
    }


    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Prescription prescription = this.repository.findById(id).orElse(null);

        if(prescription == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Cannot find prescription");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        PrescriptionResponse prescriptionResponse = new PrescriptionResponse();
        prescriptionResponse.set(prescription);
        return ResponseEntity.ok(prescriptionResponse
        );
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PrescriptionDTO prescriptionDTO) {
        Medicine medicine = this.medicineRepository.findById(prescriptionDTO.getMedicine()).orElse(null);
        if(medicine == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Cannot find medicine");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Set<Medicine> medicineSet = new HashSet<>();

        medicineSet.add(medicine);
        Prescription prescription = new Prescription(prescriptionDTO.getQuantity(), prescriptionDTO.getInstructions(), medicineSet);

        this.repository.save(prescription);

        prescription.addMedicine(medicine);

        PrescriptionResponse prescriptionResponse = new PrescriptionResponse();

        prescriptionResponse.set(prescription);

        return ResponseEntity.ok(prescriptionResponse);
    }
}
