package ar.com.dh.SistemaDeReservas.unit.service;

import ar.com.dh.dtos.requests.DentistRequestDto;
import ar.com.dh.dtos.responses.DentistResponseDto;
import ar.com.dh.entities.Dentist;
import ar.com.dh.exceptions.RequestValidationException;
import ar.com.dh.exceptions.ResourceAlreadyExistsException;
import ar.com.dh.exceptions.ResourceNotFoundException;
import ar.com.dh.repositories.IDentistRepository;
import ar.com.dh.services.impl.DentistService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DentistServiceTest {

    @Mock
    private IDentistRepository dentistRepository;
    @InjectMocks
    private DentistService dentistService;

    @BeforeEach
    public void setup() {

        dentistRepository = mock(IDentistRepository.class);
        dentistService = new DentistService(dentistRepository);
    }

    @Test
    public void createDentist() {
        //Arrange
        DentistRequestDto dentistRequestDto =
                new DentistRequestDto(1L, "Juan", "Perez", 12345L);

        //Act
        dentistService.create(dentistRequestDto);

        // Asserts
        verify(dentistRepository, times(1)).save(any(Dentist.class));
    }

    @Test
    public void methodCreateDentistThrowsResourceAlreadyExistsException() {
        //Arrange
        DentistRequestDto dentistRequestDto = new DentistRequestDto(1L, "Juan", "Perez", 12345L);

        Dentist dentistEntity = new Dentist(1L, "Juan", "Perez", 12345L);

        //Act
        when(dentistRepository.findByEnrollment(dentistRequestDto.getEnrollment())).thenReturn(Optional.of(dentistEntity));

        // Asserts
        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> {
            dentistService.create(dentistRequestDto);
        });

    }

    @Test
    public void validateEnrollmentGreaterThan5ThrowsRequestValidationException() {
        //Arrange
        DentistRequestDto dentistRequestDto = new DentistRequestDto(1L, "Juan", "Perez", 123456L);

        // Act & Asserts
        RequestValidationException exception = Assertions.assertThrows(RequestValidationException.class, () -> {
            dentistService.create(dentistRequestDto);
        });
        Assertions.assertEquals("The enrollment must have less than 5 digits", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());

    }


    @Test
    public void getOneDentist() {
        //Arrange

        Dentist dentist = new Dentist(1L, "Juan", "Perez", 12345L);
        DentistResponseDto expectedDentist = new DentistResponseDto(1L, "Juan", "Perez", 12345L);

        //Act
        Mockito.when(dentistRepository.findById(1L)).thenReturn(Optional.of(dentist));
        DentistResponseDto dentistResponseDto = dentistService.getOne(1L);

        // Asserts
        Assertions.assertEquals(expectedDentist.getEnrollment(), dentistResponseDto.getEnrollment());

    }

    @Test
    public void methodGetOneDentistThrowsResourceNotFoundException() {
        Mockito.when(dentistRepository.findById(1L)).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            dentistService.getOne(1L);
        });
    }

    @Test
    public void getAllDentists() {
        //Arrange

        Dentist dentistEntity = new Dentist(1L, "Juan", "Perez", 12345L);

        List<Dentist> dentistList = new ArrayList<>();
        dentistList.add(dentistEntity);

        DentistResponseDto expectedDentist = new DentistResponseDto(1L, "Juan", "Perez", 12345L);
        List<DentistResponseDto> dentistResponseDtoList = new ArrayList<>();
        dentistResponseDtoList.add(expectedDentist);

        //Act
        Mockito.when(dentistRepository.findAll()).thenReturn(dentistList);
        List<DentistResponseDto> dentists = dentistService.getAll();

        // Asserts
        Assertions.assertEquals(dentistResponseDtoList.size(), dentists.size());

    }

    @Test
    public void methodGetAllDentistsThrowsResourceNotFoundException() {

        Mockito.when(dentistRepository.findAll()).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            dentistService.getAll();
        });

    }

    @Test
    public void deleteDentist() {
        //Arrange
        Dentist dentistEntity = new Dentist(1L, "Juan", "Perez", 12345L);

        //Act
        Mockito.when(dentistRepository.findById(1L)).thenReturn(Optional.of(dentistEntity));
        doNothing().when(dentistRepository).delete(dentistEntity);

        dentistService.delete(1L);

        // Asserts
        verify(dentistRepository, times(1)).delete(dentistEntity);
    }

    @Test
    public void methodDeleteDentistThrowsResourceNotFoundException() {

        Mockito.when(dentistRepository.findById(1L)).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            dentistService.delete(1L);
        });
    }

    @Test
    public void updateDentist() {
        //Arrange
        Dentist dentistEntity = new Dentist(1L, "Juan", "Perez", 12345L);
        DentistRequestDto dentistRequestDto = new DentistRequestDto(1L, "Pedro", "Alvarez", 12345L);

        //Act
        Mockito.when(dentistRepository.findById(1L)).thenReturn(Optional.of(dentistEntity));

        dentistService.update(dentistRequestDto);

        // Asserts
        verify(dentistRepository, times(1)).save(any(Dentist.class));
    }

    @Test
    public void methodUpdateDentistThrowsResourceNotFoundException() {

        DentistRequestDto dentistRequestDto = new DentistRequestDto(1L, "Pedro", "Alvarez", 12345L);
        Mockito.when(dentistRepository.findById(1L)).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            dentistService.update(dentistRequestDto);
        });
    }
}