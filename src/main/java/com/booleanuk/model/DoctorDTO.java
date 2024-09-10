package com.booleanuk.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor


public class DoctorDTO {
    private int doctorId;
    private String name;
    private List<String> appointmentList;

    public DoctorDTO(int id, String name, List<String> list){
        this.doctorId = id;
        this.name = (name == null)? "" : name;
        this.appointmentList = list;
    }
}
