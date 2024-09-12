package com.booleanuk.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AppointmentDTO {
    private Integer doctor;
    private Integer patient;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date appointmentDate;
}
