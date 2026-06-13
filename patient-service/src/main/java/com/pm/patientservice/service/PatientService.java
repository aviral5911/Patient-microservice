package com.pm.patientservice.service;

import com.pm.patientservice.dto.Mapper;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.exceptions.EmailAlreadyExistException;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    @Autowired    // later professionally we use constructors to do DI instead of @Autowire
    private PatientRepository repo;

    public List<PatientResponseDTO> getAllPatient(){
        List<Patient> patients = repo.findAll();

        return patients.stream().map(patient -> Mapper.toDTO(patient)).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){

        if(repo.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistException("Patient with this email already exists");
        }

        Patient patient = repo.save(Mapper.toPatient(patientRequestDTO));

        return Mapper.toDTO(patient);
    }


}
