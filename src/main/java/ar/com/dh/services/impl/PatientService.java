package ar.com.dh.services.impl;

import ar.com.dh.dtos.requests.PatientRequestDto;
import ar.com.dh.dtos.responses.PatientResponseDto;
import ar.com.dh.entities.Patient;
import ar.com.dh.exceptions.RequestValidationException;
import ar.com.dh.exceptions.ResourceAlreadyExistsException;
import ar.com.dh.exceptions.ResourceNotFoundException;
import ar.com.dh.repositories.IPatientRepository;
import ar.com.dh.services.IService;
import ar.com.dh.util.MapperClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService implements IService<PatientResponseDto, PatientRequestDto> {

    private final IPatientRepository patientRepository;

    private static final ObjectMapper objectMapper = MapperClass.objectMapper();

    @Autowired
    public PatientService(IPatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<PatientResponseDto> getAll() {

        List<Patient> patientList = Optional.of(patientRepository.findAll()).orElseThrow(() -> new ResourceNotFoundException("No registered patients", HttpStatus.NOT_FOUND.value()));

        return patientList
                .stream()
                .map(patient -> objectMapper.convertValue(patient, PatientResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PatientResponseDto getOne(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("The patient does not exists", HttpStatus.NOT_FOUND.value()));

        return objectMapper.convertValue(patient, PatientResponseDto.class);
    }

    @Override
    public void create(PatientRequestDto patientReqDto) {
        validateDni(patientReqDto.getDni());

        Optional<Patient> patientDB = patientRepository.findByDni(patientReqDto.getDni());

        if (patientDB.isPresent()) {
            throw new ResourceAlreadyExistsException("The patient already exists", HttpStatus.CONFLICT.value());
        }

        Patient patient = objectMapper.convertValue(patientReqDto, Patient.class);
        Date date = Date.valueOf(LocalDate.now());

        patient.setRegistrationDate(date);
        patientRepository.save(patient);
    }

    @Override
    public void update(PatientRequestDto patientReqDto) {

        validateDni(patientReqDto.getDni());

        Patient patientDB = patientRepository.findByPatientIdAndAddressId(patientReqDto.getIdPatient(), patientReqDto.getAddress().getIdAddress()).orElseThrow(() -> new ResourceNotFoundException("The patient associated with that address does not exists", HttpStatus.NOT_FOUND.value()));
        Date registrationDate = patientDB.getRegistrationDate();

        patientDB = objectMapper.convertValue(patientReqDto, Patient.class);
        patientDB.setRegistrationDate(registrationDate);

        patientRepository.save(patientDB);

    }

    @Override
    public void delete(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("The patient does not exists", HttpStatus.NOT_FOUND.value()));
        patientRepository.delete(patient);
    }

    private void validateDni(String dni) {
        if (!dni.matches("[0-9]+")) {
            throw new RequestValidationException("The dni must only contain numbers", HttpStatus.BAD_REQUEST.value());
        }

        if (dni.length() < 7 || dni.length() > 8) {
            throw new RequestValidationException("The dni must have between 7 and 8 digits", HttpStatus.BAD_REQUEST.value());
        }
    }

}
