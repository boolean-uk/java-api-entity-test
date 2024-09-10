package com.booleanuk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AppointmentDTO {
    private String patientName;
    private String doctorName;
    private LocalDateTime dateTime;
}
