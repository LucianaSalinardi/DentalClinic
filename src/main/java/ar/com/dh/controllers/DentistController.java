package ar.com.dh.controllers;

import ar.com.dh.dtos.JsonMessageDto;
import ar.com.dh.dtos.requests.DentistRequestDto;
import ar.com.dh.dtos.responses.DentistResponseDto;
import ar.com.dh.services.IService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/clinical-dental/dentists")
public class DentistController {

    private final IService<DentistResponseDto, DentistRequestDto> dentistService;

    @Autowired
    public DentistController(IService<DentistResponseDto, DentistRequestDto> dentistService) {
        this.dentistService = dentistService;
    }

    @PostMapping()
    public ResponseEntity<?> createDentist(@RequestBody @Valid DentistRequestDto dentistReqDto) {
        dentistService.create(dentistReqDto);
        return new ResponseEntity<>(new JsonMessageDto("Successfully created dentist",HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DentistResponseDto> getOneDentist(@PathVariable Long id) {
        return new ResponseEntity<>(dentistService.getOne(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getAllDentists() {
        return new ResponseEntity<>(dentistService.getAll(), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<?> updateDentist(@RequestBody @Valid DentistRequestDto dentistReqDto) {
        dentistService.update(dentistReqDto);
        return new ResponseEntity<>(new JsonMessageDto("Successfully modified dentist",HttpStatus.OK.value()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDentist(@PathVariable Long id) {
        dentistService.delete(id);
        return new ResponseEntity<>(new JsonMessageDto("Successfully deleted dentist",HttpStatus.OK.value()),HttpStatus.OK);
    }
}
