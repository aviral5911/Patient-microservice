package com.pm.patientservice.service;

import com.pm.patientservice.dto.Mapper;
import com.pm.patientservice.dto.PatientDTO;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    @Autowired    // later professionally we use constructors to do DI instead of @Autowire
    private PatientRepository repo;

    public List<PatientDTO> getAllPatient(){
        List<Patient> patients = repo.findAll();

        return patients.stream().map(patient -> Mapper.toDTO(patient)).toList();
    }


}
