package ar.com.dh.services.impl;

import ar.com.dh.dtos.requests.AppointmentRequestDto;
import ar.com.dh.dtos.responses.AppointmentResponseDto;
import ar.com.dh.entities.Appointment;
import ar.com.dh.exceptions.IllegalDateException;
import ar.com.dh.exceptions.ResourceAlreadyExistsException;
import ar.com.dh.exceptions.ResourceNotFoundException;
import ar.com.dh.repositories.IAppointmentRepository;
import ar.com.dh.services.IService;
import ar.com.dh.util.MapperClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService implements IService<AppointmentResponseDto, AppointmentRequestDto> {

    private final IAppointmentRepository appointmentRepository;

    private final PatientService patientService;

    private final DentistService dentistService;
    private static final ObjectMapper objectMapper = MapperClass.objectMapper();


    @Autowired
    public AppointmentService(IAppointmentRepository appointmentRepository, PatientService patientService, DentistService dentistService) {
        this.appointmentRepository = appointmentRepository;
        this.patientService = patientService;
        this.dentistService = dentistService;
    }

    @Override
    public List<AppointmentResponseDto> getAll() {
        List<Appointment> listAppointments = appointmentRepository.findAll();

        if (listAppointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments assigned", HttpStatus.NOT_FOUND.value());
        }

        return listAppointments
                .stream()
                .map(appointment -> objectMapper.convertValue(appointment, AppointmentResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<AppointmentResponseDto> getAllAppointmentsByPatient(Long patientId) {

        patientService.getOne(patientId);

        List<Appointment> listAppointments = appointmentRepository.findAppointmentsByPatientId(patientId);

        if (listAppointments.isEmpty()) {
            throw new ResourceNotFoundException("There is no appointments registered for that patient", HttpStatus.NOT_FOUND.value());
        }

        return listAppointments
                .stream()
                .map(appointment -> objectMapper.convertValue(appointment, AppointmentResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<AppointmentResponseDto> getAllAppointmentsByDentist(Long dentistId) {

        dentistService.getOne(dentistId);
        List<Appointment> listAppointments = appointmentRepository.findAppointmentsByDentistId(dentistId);

        if (listAppointments.isEmpty()) {
            throw new ResourceNotFoundException("There is no appointments registered for that dentist", HttpStatus.NOT_FOUND.value());
        }

        return listAppointments
                .stream()
                .map(appointment -> objectMapper.convertValue(appointment, AppointmentResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponseDto getOne(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("The appointment does not exists", HttpStatus.NOT_FOUND.value()));

        return objectMapper.convertValue(appointment, AppointmentResponseDto.class);
    }

    @Override
    public void create(AppointmentRequestDto appointmentReqDto) {

        dentistService.getOne(appointmentReqDto.getDentist().getIdDentist());
        patientService.getOne(appointmentReqDto.getPatient().getIdPatient());

        if (appointmentRepository.findByPatientAndDentist(appointmentReqDto.getPatient().getIdPatient(), appointmentReqDto.getDentist().getIdDentist()).isPresent()) {
            throw new ResourceAlreadyExistsException("There is already an appointment with that patient and dentist", HttpStatus.CONFLICT.value());
        }

        List<Appointment> patientAppointmentList = appointmentRepository.findAppointmentsByPatientId(appointmentReqDto.getPatient().getIdPatient());

        if(!patientAppointmentList.isEmpty()){
            validateIfAppointmentExistsForADate(patientAppointmentList, appointmentReqDto);
        }

        Date date = appointmentReqDto.getAppointmentDate();

        validateDateLessThanCurrentOne(date.toLocalDate());

        Appointment appointment = objectMapper.convertValue(appointmentReqDto, Appointment.class);
        appointmentRepository.save(appointment);
    }

    @Override
    public void update(AppointmentRequestDto appointmentReqDto) {

        dentistService.getOne(appointmentReqDto.getDentist().getIdDentist());
        patientService.getOne(appointmentReqDto.getPatient().getIdPatient());

        Appointment appointmentDB;

        appointmentDB = appointmentRepository.findByIdAppointmentAndPatientIdAndDentistId(appointmentReqDto.getIdAppointment(), appointmentReqDto.getPatient().getIdPatient(), appointmentReqDto.getDentist().getIdDentist())
                .orElseThrow(() -> new ResourceNotFoundException("There is no appointment registered with that id for that patient and dentist", HttpStatus.NOT_FOUND.value()));



        if(appointmentDB != null) {

            List<Appointment> patientAppointmentList = appointmentRepository.findAppointmentsByPatientId(appointmentReqDto.getPatient().getIdPatient());

            if(!patientAppointmentList.isEmpty()){
                validateIfAppointmentExistsForADate(patientAppointmentList, appointmentReqDto);
            }

            Date date = appointmentReqDto.getAppointmentDate();

            validateDateLessThanCurrentOne(date.toLocalDate());

            appointmentDB = objectMapper.convertValue(appointmentReqDto, Appointment.class);
            appointmentRepository.save(appointmentDB);
        }

    }

    @Override
    public void delete(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("The appointment does not exists", HttpStatus.NOT_FOUND.value()));
        appointmentRepository.delete(appointment);
    }

    private void validateDateLessThanCurrentOne(LocalDate date){
        if ( date.isBefore(LocalDate.now())) {
            throw new IllegalDateException("Date can not be less than the current one", HttpStatus.BAD_REQUEST.value());
        }
    }

    private void validateIfAppointmentExistsForADate(List<Appointment> appointmentList, AppointmentRequestDto appointment){
        for (Appointment a: appointmentList) {
            if(appointment.getAppointmentDate().toString().equals(a.getAppointmentDate().toString())){
                throw new ResourceAlreadyExistsException("There is already an appointment for that date", HttpStatus.CONFLICT.value());
            }
        }
    }
}
