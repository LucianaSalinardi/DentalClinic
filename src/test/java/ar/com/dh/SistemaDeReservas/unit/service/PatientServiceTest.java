package ar.com.dh.SistemaDeReservas.unit.service;

import ar.com.dh.dtos.requests.AddressRequestDto;
import ar.com.dh.dtos.requests.PatientRequestDto;
import ar.com.dh.dtos.responses.AddressResponseDto;
import ar.com.dh.dtos.responses.PatientResponseDto;
import ar.com.dh.entities.Address;
import ar.com.dh.entities.Patient;
import ar.com.dh.exceptions.RequestValidationException;
import ar.com.dh.exceptions.ResourceAlreadyExistsException;
import ar.com.dh.exceptions.ResourceNotFoundException;
import ar.com.dh.repositories.IPatientRepository;
import ar.com.dh.services.impl.PatientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private IPatientRepository patientRepository;
    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    public void setup() {

        patientRepository = mock(IPatientRepository.class);
        patientService = new PatientService(patientRepository);
    }

    @Test
    public void createPatient() {
        //Arrange
        PatientRequestDto patientRequestDto =
                new PatientRequestDto(1L, "Juan", "Perez", "3465785", new AddressRequestDto(1L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina"));

        //Act
        patientService.create(patientRequestDto);

        // Asserts
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    public void methodCreatePatientThrowsResourceAlreadyExistsException() {
        //Arrange
        PatientRequestDto patientRequestDto = new PatientRequestDto(1L, "Juan", "Perez", "3465786", new AddressRequestDto(1L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina"));

        Date date = Date.valueOf(LocalDate.now());
        Address address = new Address(1L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina");
        Patient patientEntity = new Patient(1L, "Juan", "Perez", "3465785", date, address);

        //Act
        when(patientRepository.findByDni(patientRequestDto.getDni())).thenReturn(Optional.of(patientEntity));

        // Asserts
        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> {
            patientService.create(patientRequestDto);
        });

    }

    @Test
    public void validateDniThrowsRequestValidationExceptionWhenIsNotANumber() {
        //Arrange
        PatientRequestDto patientRequestDto = new PatientRequestDto(1L, "Juan", "Perez", "ghghfhg", new AddressRequestDto(1L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina"));

        // Act & Asserts
        RequestValidationException exception = Assertions.assertThrows(RequestValidationException.class, () -> {
            patientService.create(patientRequestDto);
        });
        Assertions.assertEquals("The dni must only contain numbers", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());

    }

    @Test
    public void validateDniThrowsRequestValidationExceptionWhenIsGreaterThan8() {
        //Arrange
        PatientRequestDto patientRequestDto = new PatientRequestDto(1L, "Juan", "Perez", "123456789", new AddressRequestDto(1L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina"));

        // Act & Asserts
        RequestValidationException exception = Assertions.assertThrows(RequestValidationException.class, () -> {
            patientService.create(patientRequestDto);
        });
        Assertions.assertEquals("The dni must have between 7 and 8 digits", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());

    }

    @Test
    public void getOnePatient() {
        //Arrange
        Date date = Date.valueOf(LocalDate.now());
        Address address = new Address(1L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina");
        Patient patientEntity = new Patient(1L, "Juan", "Perez", "3465786", date, address);
        AddressResponseDto addressResponseDto = new AddressResponseDto("Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina");
        PatientResponseDto expectedPatient = new PatientResponseDto(1L, "Juan", "Perez", "3465786", addressResponseDto);

        //Act
        Mockito.when(patientRepository.findById(1L)).thenReturn(Optional.of(patientEntity));
        PatientResponseDto patientResponseDto = patientService.getOne(1L);

        // Asserts
        Assertions.assertEquals(expectedPatient.getDni(), patientResponseDto.getDni());

    }

    @Test
    public void methodGetOnePatientThrowsResourceNotFoundException() {
        Mockito.when(patientRepository.findById(1L)).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            patientService.getOne(1L);
        });
    }

    @Test
    public void getAllPatients() {
        //Arrange
        Date date = Date.valueOf(LocalDate.now());
        Address address = new Address(1L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina");
        Patient patientEntity = new Patient(1L, "Juan", "Perez", "3465786", date, address);
        AddressResponseDto addressResponseDto = new AddressResponseDto("Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina");

        List<Patient> patientList = new ArrayList<>();
        patientList.add(patientEntity);

        PatientResponseDto expectedPatient = new PatientResponseDto(1L, "Juan", "Perez", "3465786", addressResponseDto);
        List<PatientResponseDto> patientResponseDtoList = new ArrayList<>();
        patientResponseDtoList.add(expectedPatient);

        //Act
        Mockito.when(patientRepository.findAll()).thenReturn(patientList);
        List<PatientResponseDto> patients = patientService.getAll();

        // Asserts
        Assertions.assertEquals(patientResponseDtoList.size(), patients.size());

    }

    @Test
    public void methodGetAllPatientsThrowsResourceNotFoundException() {

        Mockito.when(patientRepository.findAll()).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            patientService.getAll();
        });

    }

    @Test
    public void deletePatient() {
        //Arrange
        Date date = Date.valueOf(LocalDate.now());
        Address address = new Address(1L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina");
        Patient patientEntity = new Patient(1L, "Juan", "Perez", "3465786", date, address);

        //Act
        Mockito.when(patientRepository.findById(1L)).thenReturn(Optional.of(patientEntity));
        doNothing().when(patientRepository).delete(patientEntity);

        patientService.delete(1L);

        // Asserts
        verify(patientRepository, times(1)).delete(patientEntity);
    }

    @Test
    public void methodDeletePatientThrowsResourceNotFoundException() {

        Mockito.when(patientRepository.findById(1L)).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            patientService.delete(1L);
        });
    }

    @Test
    public void updatePatient() {
        //Arrange
        Date date = Date.valueOf(LocalDate.now());
        Address address = new Address(1L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina");
        Patient patientEntity = new Patient(1L, "Juan", "Perez", "3465786", date, address);
        PatientRequestDto patientRequestDto = new PatientRequestDto(1L, "Pedro", "Alvarez", "3465786", new AddressRequestDto(1L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina"));

        //Act
        Mockito.when(patientRepository.findByPatientIdAndAddressId(1L, 1L)).thenReturn(Optional.of(patientEntity));

        patientService.update(patientRequestDto);

        // Asserts
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    public void methodUpdateAPatientThrowsResourceNotFoundException() {

        PatientRequestDto patientRequestDto = new PatientRequestDto(1L, "Pedro", "Alvarez", "3465786", new AddressRequestDto(1L, "Corrientes", 345, "CABA", "Buenos Aires", 1413, "Argentina"));
        Mockito.when(patientRepository.findByPatientIdAndAddressId(1L, 1L)).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            patientService.update(patientRequestDto);
        });
    }
}