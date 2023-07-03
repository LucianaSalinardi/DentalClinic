package ar.com.dh.login;

import ar.com.dh.login.entities.Role;
import ar.com.dh.login.entities.User;
import ar.com.dh.login.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataLoader implements ApplicationRunner {

    private final UserRepository userRepository;

    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * ACLARACION: abrir localhost:8080/login en lugar de localhost:8080 para que funcione.
     **/
    @Override
    public void run(ApplicationArguments args){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Optional<User> userDB = userRepository.findByUsername("luu");

        if(userDB.isEmpty()){
            String passUser = passwordEncoder.encode("luciana");
            User user = new User("Luciana", "lu.salinardi@gmail.com", "luu", passUser, Role.USER);
            userRepository.save(user);
        }

        Optional<User> adminDB = userRepository.findByUsername("admin");

        if(adminDB.isEmpty()){
            String passAdmin = passwordEncoder.encode("admin");
            User user = new User("Admin", "admin@gmail.com", "admin", passAdmin, Role.ADMIN);
            userRepository.save(user);
        }
    }
}
