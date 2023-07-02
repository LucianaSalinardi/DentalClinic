package ar.com.dh.services.impl;

import ar.com.dh.dtos.requests.DentistRequestDto;
import ar.com.dh.dtos.responses.DentistResponseDto;
import ar.com.dh.entities.Dentist;
import ar.com.dh.exceptions.RequestValidationException;
import ar.com.dh.exceptions.ResourceAlreadyExistsException;
import ar.com.dh.exceptions.ResourceNotFoundException;
import ar.com.dh.repositories.IDentistRepository;
import ar.com.dh.services.IService;
import ar.com.dh.util.MapperClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DentistService implements IService<DentistResponseDto, DentistRequestDto> {

    private final IDentistRepository dentistRepository;

    private static final ObjectMapper objectMapper = MapperClass.objectMapper();

    @Autowired
    public DentistService(IDentistRepository dentistRepository) {
        this.dentistRepository = dentistRepository;
    }


    @Override
    public DentistResponseDto getOne(Long id) {
        Dentist dentist = dentistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("The dentist does not exists", HttpStatus.NOT_FOUND.value()));

        return objectMapper.convertValue(dentist, DentistResponseDto.class);
    }

    @Override
    public List<DentistResponseDto> getAll() {
        List<Dentist> listDentist = Optional.of(dentistRepository.findAll()).orElseThrow(() -> new ResourceNotFoundException("No registered dentists", HttpStatus.NOT_FOUND.value()));

        return listDentist
                .stream()
                .map(dentist -> objectMapper.convertValue(dentist, DentistResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void create(DentistRequestDto dentistReqDto) {

        validateEnrollment(dentistReqDto.getEnrollment());

        if (dentistRepository.findByEnrollment(dentistReqDto.getEnrollment()).isPresent()) {
            throw new ResourceAlreadyExistsException("The dentist already exists", HttpStatus.CONFLICT.value());
        }
        Dentist dentist = objectMapper.convertValue(dentistReqDto, Dentist.class);

        dentistRepository.save(dentist);
    }

    @Override
    public void update(DentistRequestDto dentistReqDto) {

        validateEnrollment(dentistReqDto.getEnrollment());

        Dentist dentistDB = dentistRepository.findById(dentistReqDto.getIdDentist()).orElseThrow(() -> new ResourceNotFoundException("The dentist does not exists", HttpStatus.NOT_FOUND.value()));

        if (dentistDB != null) {
            dentistDB = objectMapper.convertValue(dentistReqDto, Dentist.class);

            dentistRepository.save(dentistDB);
        }

    }

    @Override
    public void delete(Long id) {
        Dentist dentist = dentistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("The dentist does not exists", HttpStatus.NOT_FOUND.value()));
        dentistRepository.delete(dentist);
    }

    private void validateEnrollment(Long enrollment) {
        if (String.valueOf(enrollment).length() > 5) {
            throw new RequestValidationException("The enrollment must have less than 5 digits", HttpStatus.BAD_REQUEST.value());
        }
    }
}
