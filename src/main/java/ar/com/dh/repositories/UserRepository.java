package ar.com.dh.repositories;

import ar.com.dh.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByEmailAndPassword(String email, String password);
}
