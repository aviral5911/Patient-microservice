package com.pm.patientservice.dto;

import com.pm.patientservice.model.Patient;

public class Mapper {

    public static PatientDTO toDTO(Patient patient){
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId().toString());
        dto.setName(patient.getName());
        dto.setEmail(patient.getEmail());
        dto.setAddress(patient.getAddress());
        dto.setDateOfBirth(patient.getDateOfBirth().toString());

        return dto;
    }
}
