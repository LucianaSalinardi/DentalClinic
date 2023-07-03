package ar.com.dh.login.service;


import ar.com.dh.login.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

         ar.com.dh.login.entities.User user = userRepository.findByUsername(username).get();

        UserDetails userDetails = new User(username, user.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));

        return userDetails;
    }
}
