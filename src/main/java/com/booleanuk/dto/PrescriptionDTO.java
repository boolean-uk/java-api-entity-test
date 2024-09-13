package com.booleanuk.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionDTO {
    private Integer medicine;

    private  Integer quantity;

    private String instructions;

    private Integer appointment;
}
