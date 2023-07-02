package ar.com.dh.repositories;

import ar.com.dh.entities.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDentistRepository extends JpaRepository <Dentist,Long> {

    @Query("SELECT d FROM Dentist d WHERE d.enrollment = ?1")
    Optional<Dentist> findByEnrollment(Long enrollment);
}
