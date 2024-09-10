package com.booleanuk.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter

public class PatientDTO {

    public PatientDTO(int id, String name, Date dateOfBirth, List<String> appointments) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.appointments = appointments;
    }

    private int id;

    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateOfBirth;

    private List<String> appointments;
}
