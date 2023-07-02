package ar.com.dh.repositories;

import ar.com.dh.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IAppointmentRepository extends JpaRepository <Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.patient.idPatient = ?1 AND a.dentist.idDentist = ?2")
    Optional<Appointment> findByPatientAndDentist(Long idPatient, Long idDentist);

    @Query("SELECT a FROM Appointment a WHERE a.patient.idPatient = ?1")
    List<Appointment> findAppointmentsByPatientId(Long idPatient);

    @Query("SELECT a FROM Appointment a WHERE a.idAppointment = ?1 AND a.patient.idPatient = ?2 AND a.dentist.idDentist = ?3")
    Optional<Appointment> findByIdAppointmentAndPatientIdAndDentistId(Long id, Long idPatient, Long idDentist);

    @Query("SELECT a FROM Appointment a WHERE a.dentist.idDentist = ?1")
    List<Appointment> findAppointmentsByDentistId(Long idDentist);

}
