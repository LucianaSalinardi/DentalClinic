package ar.com.dh.controllers;

import ar.com.dh.dtos.JsonMessageDto;
import ar.com.dh.dtos.requests.PatientRequestDto;
import ar.com.dh.dtos.responses.PatientResponseDto;
import ar.com.dh.services.IService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/clinical-dental/patients")
public class PatientController {

    private final IService<PatientResponseDto, PatientRequestDto> patientService;

    @Autowired
    public PatientController(IService<PatientResponseDto, PatientRequestDto> patientService) {
        this.patientService = patientService;
    }

    @PostMapping()
    public ResponseEntity<?> createPatient(@RequestBody @Valid PatientRequestDto patientReqDto) {
        patientService.create(patientReqDto);
        return new ResponseEntity<>(new JsonMessageDto("Successfully created patient", HttpStatus.CREATED.value()),HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getOnePatient(@PathVariable Long id) {
        return new ResponseEntity<>(patientService.getOne(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        return new ResponseEntity<>(patientService.getAll(), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<?> updatePatient(@RequestBody @Valid PatientRequestDto patientReqDto) {
        patientService.update(patientReqDto);
        return new ResponseEntity<>(new JsonMessageDto("Successfully modified patient",HttpStatus.OK.value()),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id) {
        patientService.delete(id);
        return new ResponseEntity<>(new JsonMessageDto("Successfully deleted patient",HttpStatus.OK.value()),HttpStatus.OK);
    }
}
