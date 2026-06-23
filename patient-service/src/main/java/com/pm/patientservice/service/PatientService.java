package com.pm.patientservice.service;

import com.pm.patientservice.KafkaProducer;
import com.pm.patientservice.dto.Mapper;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientUpdateRequestDTO;
import com.pm.patientservice.exceptions.EmailAlreadyExistException;
import com.pm.patientservice.exceptions.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    @Autowired    // later professionally we use constructors to do DI instead of @Autowire
    private PatientRepository repo;

    @Autowired
    BillingServiceGrpcClient grpcClient;

    @Autowired
    KafkaProducer kafkaProducer;

    // get all patients
    public List<PatientResponseDTO> getAllPatient(){
        List<Patient> patients = repo.findAll();

        return patients.stream().map(patient -> Mapper.toDTO(patient)).toList();
    }

    // add a patient
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){

        if(repo.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistException("Patient with this email already exists");
        }

        Patient patient = repo.save(Mapper.toPatient(patientRequestDTO));

        grpcClient.createBillingAccount(patient.getId().toString() , patient.getName() , patient.getEmail());

        kafkaProducer.sendEvent(patient);

        return Mapper.toDTO(patient);
    }

    // update a patient
    public PatientResponseDTO updatePatient(UUID id , PatientUpdateRequestDTO patientRequestDTO){
        Patient patient = repo.findById(id)
                .orElseThrow(() ->new PatientNotFoundException("No patient with id" + id));

        if(repo.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)){
            throw new EmailAlreadyExistException("This email already exists");
        }
        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updated = repo.save(patient);
        return Mapper.toDTO(updated);

    }

    public void deletePatient(UUID id) {
        repo.deleteById(id);
    }

    public PatientResponseDTO getbyID(UUID id) {
        Patient patient = repo.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("No Patient exists with this id"));

        return Mapper.toDTO(patient);

    }
}
