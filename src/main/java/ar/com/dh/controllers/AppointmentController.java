package ar.com.dh.controllers;

import ar.com.dh.dtos.JsonMessageDto;
import ar.com.dh.dtos.requests.AppointmentRequestDto;
import ar.com.dh.dtos.responses.AppointmentResponseDto;
import ar.com.dh.services.IService;
import ar.com.dh.services.impl.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clinical-dental/appointments")
public class AppointmentController {

    private final IService<AppointmentResponseDto, AppointmentRequestDto> iAppointmentService;
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(IService<AppointmentResponseDto, AppointmentRequestDto> iAppointmentService, AppointmentService appointmentService) {
        this.iAppointmentService = iAppointmentService;
        this.appointmentService = appointmentService;
    }

    @PostMapping()
    public ResponseEntity<?> createAppointment(@RequestBody @Valid AppointmentRequestDto appointmentReqDto) {
        iAppointmentService.create(appointmentReqDto);
        return new ResponseEntity<>(new JsonMessageDto("Successfully created appointment", HttpStatus.CREATED.value()), HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> getOneAppointment(@PathVariable Long id) {
        return new ResponseEntity<>(iAppointmentService.getOne(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {
        return new ResponseEntity<>(iAppointmentService.getAll(), HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<?> updateAppointment(@RequestBody @Valid AppointmentRequestDto appointmentReqDto) {
        iAppointmentService.update(appointmentReqDto);
        return new ResponseEntity<>(new JsonMessageDto("Successfully modified appointment", HttpStatus.OK.value()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
        iAppointmentService.delete(id);
        return new ResponseEntity<>(new JsonMessageDto("Successfully deleted appointment", HttpStatus.OK.value()), HttpStatus.OK);
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointmentsByPatient(@PathVariable Long id) {
        return new ResponseEntity<>(appointmentService.getAllAppointmentsByPatient(id), HttpStatus.OK);
    }

    @GetMapping("/dentists/{id}")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointmentsByDentist(@PathVariable Long id) {
        return new ResponseEntity<>(appointmentService.getAllAppointmentsByDentist(id), HttpStatus.OK);
    }
}
