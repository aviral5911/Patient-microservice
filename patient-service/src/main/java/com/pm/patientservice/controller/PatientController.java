package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientDTO;
import com.pm.patientservice.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService service;

    @GetMapping
    public List<PatientDTO> getAll(){
        return service.getAllPatient();
    }
}
