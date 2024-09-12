package com.booleanuk.controller;

import com.booleanuk.Response.*;
import com.booleanuk.dto.PrescriptionDTO;
import com.booleanuk.model.Medicine;
import com.booleanuk.model.Prescription;
import com.booleanuk.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("medicines")
public class MedicineController {
    @Autowired
    MedicineRepository repository;


    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Medicine> medicines = this.repository.findAll();

        if(medicines.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Cannot find medicines");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        MedicineListResponse medicineListResponse = new MedicineListResponse();
        medicineListResponse.set(medicines);
        return ResponseEntity.ok(medicineListResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Medicine medicine = this.repository.findById(id).orElse(null);

        if(medicine == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Cannot find medicine");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        MedicineResponse medicineResponse = new MedicineResponse();
        medicineResponse.set(medicine);
        return ResponseEntity.ok(medicineResponse);
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody Medicine medicine) {
        if(medicine.getName() == null || medicine.getTypeOfMedicine() == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Cannot find medicine");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        this.repository.save(medicine);
        MedicineResponse medicineResponse = new MedicineResponse();
        medicineResponse.set(medicine);

        return ResponseEntity.ok(medicineResponse);
    }
}
